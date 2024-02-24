package com.r3.developers.serviceRequest.workflows

import com.r3.developers.serviceRequest.contracts.BatchContract
import com.r3.developers.serviceRequest.contracts.SaleContract
import com.r3.developers.serviceRequest.contracts.ServiceRequestContract
import com.r3.developers.serviceRequest.models.ProformaInvoiceModel
import com.r3.developers.serviceRequest.models.ResponseModel3
import com.r3.developers.serviceRequest.models.ShipOrderModel
import com.r3.developers.serviceRequest.states.BatchState
import com.r3.developers.serviceRequest.states.SaleState
import com.r3.developers.serviceRequest.states.ServiceRequestState
import net.corda.v5.application.flows.*
import net.corda.v5.application.marshalling.JsonMarshallingService
import net.corda.v5.application.membership.MemberLookup
import net.corda.v5.application.messaging.FlowMessaging
import net.corda.v5.application.messaging.FlowSession
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.base.types.MemberX500Name
import net.corda.v5.ledger.common.NotaryLookup
import net.corda.v5.ledger.utxo.UtxoLedgerService
import net.corda.v5.ledger.utxo.transaction.UtxoTransactionBuilder
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@InitiatingFlow(protocol = "Ship-Order-flow")
class ShipOrderFlow : ClientStartableFlow {

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    @CordaInject
    lateinit var flowMessaging: FlowMessaging

    @CordaInject
    lateinit var memberLookup: MemberLookup

    @CordaInject
    lateinit var notaryLookup: NotaryLookup

    @CordaInject
    lateinit var utxoLedgerService: UtxoLedgerService

    @Suspendable
    override fun call(requestBody: ClientRequestBody): String {
        val request = requestBody.getRequestBodyAs(jsonMarshallingService, ShipOrderModel::class.java)


        //val requestId = request.requestId
        val saleId = request.saleId
        val notary = notaryLookup.notaryServices.single()

        val saleRequestStateAndRef = utxoLedgerService.findUnconsumedStatesByType(SaleState::class.java)
            .firstOrNull { stateAndRef -> stateAndRef.state.contractState.saleId == saleId }
            ?: throw IllegalArgumentException("No state matching the saleId $saleId")

        val inputSaleRequestStateRef = saleRequestStateAndRef.ref
        val outputSaleRequestStateData = saleRequestStateAndRef.state.contractState


        outputSaleRequestStateData.shipOrderData= request
        outputSaleRequestStateData.updatedOn = Instant.now().toEpochMilli()
        outputSaleRequestStateData.status ="ship-order-data"

        val harvesterName = MemberX500Name.parse("CN=Harvester, OU=Test Dept, O=R3, L=London, C=GB")
        val harvester = memberLookup.lookup(harvesterName)
            ?: throw CordaRuntimeException("MemberLookup can't find harvester specified in flow arguments.")
        val lspName = MemberX500Name.parse("CN=LSP, OU=Test Dept, O=R3, L=London, C=GB")
        val lsp = memberLookup.lookup(lspName)
            ?: throw CordaRuntimeException("MemberLookup can't find lsp specified in flow arguments.")
        val collectorName = MemberX500Name.parse("CN=Collector, OU=Test Dept, O=R3, L=London, C=GB")
        val collector = memberLookup.lookup(collectorName)
            ?: throw CordaRuntimeException("MemberLookup can't find collector specified in flow arguments.")
        val brokerName = MemberX500Name.parse("CN=Broker, OU=Test Dept, O=R3, L=London, C=GB")
        val broker = memberLookup.lookup(brokerName)
            ?: throw CordaRuntimeException("MemberLookup can't find otherMember specified in flow arguments.")

        val participants = listOf(
            harvester.ledgerKeys.first(),
            lsp.ledgerKeys.first(),
            collector.ledgerKeys.first(),
            broker.ledgerKeys.first()
        )


        val transaction = utxoLedgerService.createTransactionBuilder()
            .setNotary(notary.name)
            .addInputState(inputSaleRequestStateRef)
            .addOutputState(outputSaleRequestStateData)
            .addCommand(SaleContract.Update())
            .setTimeWindowUntil(Instant.now().plus(1, ChronoUnit.DAYS))
            .addSignatories(participants)
            .toSignedTransaction()

        val collectorSession = flowMessaging.initiateFlow(collectorName)
        val harvesterSession = flowMessaging.initiateFlow(harvesterName)
        val lspSession = flowMessaging.initiateFlow(lspName)
        try{
            val transactionId = transaction.id.toString().removePrefix("SHA-256D:")
            //val saleId= uuid// Set the actual requestId based on your business logic
            val finalizeResult =
                utxoLedgerService.finalize(transaction, listOf(collectorSession, lspSession, harvesterSession)).toString()

            // Return the ServiceRequestResponse with transactionId, requestId, and no error
            return ResponseModel3(transactionId, saleId, null).toString()
        } catch (e: Exception) {
            // Return the ServiceRequestResponse with transactionId, empty requestId, and the error message
            return ResponseModel3(null,null,"Flow failed, message: ${e.message}").toString()
        }
    }


}
@InitiatedBy(protocol = "Ship-Order-flow")
class SaleStateResponderFlow: ResponderFlow {

    @CordaInject
    lateinit var utxoLedgerService: UtxoLedgerService

    @Suspendable
    override fun call(session: FlowSession) {
        // Receive, verify, validate, sign and record the transaction sent from the initiator
        utxoLedgerService.receiveFinality(session) {transaction ->
            /*
             * [receiveFinality]
will automatically verify the transaction and its signatures before signing it.
             * However, just because a transaction is contractually valid doesn't mean we necessarily want to sign.
             * What if we don't want to deal with the counterparty in question, or the value is too high,
             * or we're not happy with the transaction's structure? [UtoTransactionValidator] (the lambda created
             * here) allows us to define the additional checks. If any of these conditions are not met,
             * we will not sign the transaction - even if the transaction and its signatures are contractually valid.
             */
        }
    }
}
