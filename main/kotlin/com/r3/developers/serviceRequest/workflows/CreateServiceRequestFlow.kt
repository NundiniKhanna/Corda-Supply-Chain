package com.r3.developers.serviceRequest.workflows



import com.r3.developers.serviceRequest.contracts.ServiceRequestContract
import com.r3.developers.serviceRequest.models.CreateServiceRequestModel
import com.r3.developers.serviceRequest.models.ResponseModel1
//import com.r3.developers.serviceRequest.models.FlowResult
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
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


private fun UUID.toInt() {

}

@InitiatingFlow(protocol = "Create-Service-request")
class CreateServiceRequestFlow() : ClientStartableFlow {


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
        val request = requestBody.getRequestBodyAs(jsonMarshallingService, CreateServiceRequestModel::class.java)


        val notary = notaryLookup.notaryServices.single()


        val harvesterName = MemberX500Name.parse("CN=Harvester, OU=Test Dept, O=R3, L=London, C=GB")
        val harvester = memberLookup.lookup(harvesterName)
            ?: throw CordaRuntimeException("MemberLookup can't find otherMember specified in flow arguments.")
        val lspName = MemberX500Name.parse("CN=LSP, OU=Test Dept, O=R3, L=London, C=GB")
        val lsp = memberLookup.lookup(lspName)
            ?: throw CordaRuntimeException("MemberLookup can't find otherMember specified in flow arguments.")
        val collectorName = MemberX500Name.parse("CN=Collector, OU=Test Dept, O=R3, L=London, C=GB")
        val collector = memberLookup.lookup(collectorName)
            ?: throw CordaRuntimeException("MemberLookup can't find otherMember specified in flow arguments.")
        val brokerName = MemberX500Name.parse("CN=Broker, OU=Test Dept, O=R3, L=London, C=GB")
        val broker = memberLookup.lookup(brokerName)
            ?: throw CordaRuntimeException("MemberLookup can't find otherMember specified in flow arguments.")

        val uuid = UUID.randomUUID()
        val  createdOn = Instant.now().toEpochMilli()
        val serviceRequest = ServiceRequestState(
            //id = UUID.randomUUID(),
            createServiceRequestModel = request,

            status = "created",
            requestId = uuid,
            //batchId = uuid,
            // saleId = request.saleId,
           // createdOn = Instant.now().toEpochMilli(),
            updatedOn = Instant.now().toEpochMilli(),

            harvesterPublicKey = harvester.ledgerKeys.first(),
            lspPublicKey = lsp.ledgerKeys.first(),
            collectorPublicKey = collector.ledgerKeys.first(),
            brokerPublicKey = broker.ledgerKeys.first(),
            participants = listOf(
                harvester.ledgerKeys.first(),
                lsp.ledgerKeys.first(),
                collector.ledgerKeys.first(),
                broker.ledgerKeys.first()
            )
        )

        val transaction = utxoLedgerService.createTransactionBuilder()
            .setNotary(notary.name)
            .addOutputState(serviceRequest)
            .addCommand(ServiceRequestContract.Create())
            .setTimeWindowUntil(Instant.now().plus(1, ChronoUnit.DAYS))
            .addSignatories(serviceRequest.participants)
            .toSignedTransaction()


        val collectorSession = flowMessaging.initiateFlow(collectorName)
        val lspSession = flowMessaging.initiateFlow(lspName)
        val brokerSession = flowMessaging.initiateFlow(brokerName)
        try {
            val transactionId = transaction.id.toString().removePrefix("SHA-256D:")
            val requestId= uuid// Set the actual requestId based on your business logic
            val finalizeResult =
                utxoLedgerService.finalize(transaction, listOf(collectorSession, lspSession, brokerSession)).toString()

            // Return the ServiceRequestResponse with transactionId, requestId, and no error
            return ResponseModel1(transactionId, requestId, null).toString()
        } catch (e: Exception) {
            // Return the ServiceRequestResponse with transactionId, empty requestId, and the error message
            return ResponseModel1(null,null,"Flow failed, message: ${e.message}").toString()
        }
    }


}
//data class ServiceRequestResponse(val transactionId: String, val requestId: String, val error: String?)

    //data class CreateServiceResponse(val transactionId: String, val finalizeResult: String)

    @InitiatedBy(protocol = "Create-Service-request")
    class ServiceRequestResponderFlow() : ResponderFlow {

        @CordaInject
        lateinit var utxoLedgerService: UtxoLedgerService

        @Suspendable
        override fun call(session: FlowSession) {
            // Receive, verify, validate, sign and record the transaction sent from the initiator
            utxoLedgerService.receiveFinality(session) { transaction ->
                /*
             * [receiveFinality]
will automatically verify the transaction and its signatures before signing it.
          Flow.   * However, just because a transaction is contractually valid doesn't mean we necessarily want to sign.
             * What if we don't want to deal with the counterparty in question, or the value is too high,
             * or we're not happy with the transaction's structure? [UtoTransactionValidator] (the lambda created
             * here) allows us to define the additional checks. If any of these conditions are not met,
             * we will not sign the transaction - even if the transaction and its signatures are contractually valid.

             */



            }
            //val finalizeResult = "Service Request Transaction Completed Successfully"


           // val response = CreateServiceResponse(transactionId, finalizeResult)
            //session.send(response)
        }
    }


