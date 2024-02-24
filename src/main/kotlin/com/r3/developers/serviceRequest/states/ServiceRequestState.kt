package com.r3.developers.serviceRequest.states



import com.r3.developers.serviceRequest.contracts.ServiceRequestContract
import com.r3.developers.serviceRequest.models.*
//import com.r3.developers.serviceRequest.models.GetServiceRequestBody
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey
import java.util.*

@BelongsToContract(ServiceRequestContract::class)
data class ServiceRequestState(

    var createServiceRequestModel: CreateServiceRequestModel?=null,
    var acceptServiceRequestModel: AcceptServiceRequestModel?=null,
    var enrouteToProducerModel: EnrouteToProducerModel?=null,
    var arrivedOnProducerFarmModel: ArrivedOnProducerFarmModel?=null,
    var enrouteToPackHouseModel: EnrouteToPackHouseModel?=null,
    var arrivedAtPackHouseModel: ArrivedAtPackHouseModel?=null,
   // var updateBatchIdModel: UpdateBatchIdModel?=null,
    var fruitFlowModel: FruitFlowModel?=null,
    var reponseModel: ResponseModel1?=null,
    var status: String,
    val requestId: UUID = UUID.randomUUID(),
    var updatedOn: Long?=null,
    var batchId:  UUID?=null,
   // var updateBatchIdModel: UpdateBatchIdModel?=null,
   // var arrivedAtPackHouseOn: Long?=null,
    //var enrouteToPackHouseOn:Long?=null,
    //var enrouteToProducerOn:Long?=null,
   // var assignedOn: Long?=null,
   // var createdOn: Long?=null,
    val harvesterPublicKey: PublicKey,
    val lspPublicKey: PublicKey,
    val collectorPublicKey: PublicKey,
    val brokerPublicKey:PublicKey,
    private val participants: List<PublicKey>
) : ContractState {

    override fun getParticipants(): List<PublicKey> = participants
//    fun matchesFilter(requestId: String,  batchState: BatchState): Boolean {
//        return requestId in batchState.serviceRequestIDList
//    }



}
