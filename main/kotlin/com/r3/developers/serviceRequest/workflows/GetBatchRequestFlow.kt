package com.r3.developers.serviceRequest.workflows
import com.r3.developers.serviceRequest.contracts.ServiceRequestContract
import com.r3.developers.serviceRequest.models.AcceptServiceRequestModel
import com.r3.developers.serviceRequest.models.CreateServiceRequestModel
import com.r3.developers.serviceRequest.states.BatchState
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
import java.security.PublicKey
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@InitiatingFlow(protocol = "Get-Batch-request")
class GetBatchRequestFlow : ClientStartableFlow {

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    @CordaInject
    lateinit var flowMessaging: FlowMessaging


    @CordaInject
    lateinit var notaryLookup: NotaryLookup

    @CordaInject

    lateinit var utxoLedgerService: UtxoLedgerService

    @Suspendable
    override fun call(requestBody: ClientRequestBody): String {
        val getRequest = requestBody.getRequestBodyAs(jsonMarshallingService, GetBatchRequestRequestBody::class.java)
        val batchId = getRequest.batchId
        // Replace with actual input
        val notary = notaryLookup.notaryServices.single()

        val batchRequestStateAndRef = utxoLedgerService.findUnconsumedStatesByType(BatchState::class.java)
            .filter { stateAndRef -> stateAndRef.state.contractState.batchId == batchId }
            .firstOrNull()
            ?: throw IllegalArgumentException("No state matching the requestId $batchId")

        val batchRequestStateData = batchRequestStateAndRef.state.contractState
        val responseData = "Batch Request Data: $batchRequestStateData"


        try {
            return responseData

        } catch (e: Exception) {
            return "Flow failed, message: ${e.message}"

        }
    }
}

class GetBatchRequestRequestBody (
    val batchId: UUID
)
