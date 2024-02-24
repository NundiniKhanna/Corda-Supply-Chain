package com.r3.developers.serviceRequest.workflows



import com.r3.developers.serviceRequest.contracts.BatchContract
import com.r3.developers.serviceRequest.models.CreateBatchStateModel
import com.r3.developers.serviceRequest.models.ResponseModel1
import com.r3.developers.serviceRequest.models.ResponseModel2
import com.r3.developers.serviceRequest.states.BatchState
import com.r3.developers.serviceRequest.workflows.CreateServiceRequestFlow

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


@InitiatingFlow(protocol = "Create-batch-state")
class CreateBatchStateFlow : ClientStartableFlow {


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
    override fun call(requestBody: ClientRequestBody) :String {
        val request = requestBody.getRequestBodyAs(jsonMarshallingService, CreateBatchStateModel::class.java)



        val uuid= UUID.randomUUID()
        val arrayOfRequestIds = request.serviceRequestIDList
        val notary = notaryLookup.notaryServices.single()


        val filteredRequestIds = mutableListOf<UUID>()
        for (requestId in arrayOfRequestIds) {
            val serviceRequestStateAndRef = utxoLedgerService.findUnconsumedStatesByType(ServiceRequestState::class.java)
                .firstOrNull { stateAndRef ->
                    stateAndRef.state.contractState.requestId == requestId
                }

            if (serviceRequestStateAndRef != null) {
                // RequestId matches the filter criteria, add it to the filtered list
                filteredRequestIds.add(requestId)
                val inputServiceRequestStateRef = serviceRequestStateAndRef.ref
                val outputServiceRequestStateData = serviceRequestStateAndRef.state.contractState

                // outputServiceRequestStateData.acceptServiceRequestModel= request
                outputServiceRequestStateData.updatedOn = Instant.now().toEpochMilli()
                outputServiceRequestStateData.status = "created-batch-request"
                outputServiceRequestStateData.batchId = uuid
            } else {
                // RequestId not found, throw an exception or handle it accordingly
                throw IllegalArgumentException("No state matching the requestId $requestId")
            }
        }



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

       val createdOn = Instant.now().toEpochMilli()
        val recordedOn = Instant.now().toEpochMilli()
        val batchRequest = BatchState(

            createBatchStateModel = request,
            serviceRequestIDList = filteredRequestIds,
            totalNoOfBoxes = request.totalNoOfBoxes,
            status = "created-batch-request",
           // requestId = request.requestId,
            batchId = uuid,
            saleId = uuid,
            //saleId = request.saleId,
           // proFormaUpdatedOn = Instant.now().toEpochMilli(),
            //createdOn = Instant.now().toEpochMilli(),
            //recordedOn = Instant.now().toEpochMilli(),
            harvester = harvester.ledgerKeys.first(),
            lsp = lsp.ledgerKeys.first(),
            collector = collector.ledgerKeys.first(),
            broker = collector.ledgerKeys.first(),
            participants = listOf( harvester.ledgerKeys.first(), lsp.ledgerKeys.first(), collector.ledgerKeys.first(), broker.ledgerKeys.first())
        )
       // subFlow(UpdateBatchIdAgain(batchId))
      //  subFlow(UpdateBatchIdAgain.Initiator(requestId, "Batch Created"))
        val transaction = utxoLedgerService.createTransactionBuilder()
            .setNotary(notary.name)
            .addOutputState(batchRequest)
            .addCommand(BatchContract.Create())
            .setTimeWindowUntil(Instant.now().plus( 1, ChronoUnit.DAYS))
            .addSignatories(batchRequest.participants)
            .toSignedTransaction()

        val collectorSession = flowMessaging.initiateFlow(collectorName)
        val harvesterSession = flowMessaging.initiateFlow(harvesterName)
        val brokerSession = flowMessaging.initiateFlow(brokerName)
        try{
            val transactionId = transaction.id.toString().removePrefix("SHA-256D:")
            val batchId= uuid// Set the actual requestId based on your business logic
            val finalizeResult =
                utxoLedgerService.finalize(transaction, listOf(collectorSession, harvesterSession, brokerSession)).toString()

           // val createServiceRequestFlow = CreateServiceRequestFlow(batchId)
           // subFlow(createServiceRequestFlow)
            // Return the ServiceRequestResponse with transactionId, requestId, and no error
            return ResponseModel2(transactionId, batchId, null).toString()
        } catch (e: Exception) {
            // Return the ServiceRequestResponse with transactionId, empty requestId, and the error message
            return ResponseModel2(null,null,"Flow failed, message: ${e.message}").toString()
        }
    }


}
@InitiatedBy(protocol = "Create-batch-state")
class BatchStatetResponderFlow: ResponderFlow {

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