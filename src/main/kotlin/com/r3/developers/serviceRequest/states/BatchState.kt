package com.r3.developers.serviceRequest.states

import com.r3.developers.serviceRequest.contracts.BatchContract
import com.r3.developers.serviceRequest.models.*
//import com.r3.developers.serviceRequest.models.GetServiceRequestBody
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey

import java.util.*

@BelongsToContract(BatchContract::class)
data class BatchState(
    val serviceRequestIDList: MutableList<UUID>,
    val batchId: UUID = UUID.randomUUID(),
    val harvester: PublicKey,
    val lsp: PublicKey,
    val collector: PublicKey,
    val broker: PublicKey,
    var proformaInvoiceModel: ProformaInvoiceModel? = null,
    var createBatchStateModel: CreateBatchStateModel?=null,
    var updateBatchModel: UpdateBatchModel? = null,
  //  var updateBatchIdModel: UpdateBatchIdModel?=null,
    // val createdOn: Long,
    // var recordedOn: Long,
    // val totalWeight: Double,
    // val requestId: String,
    // val saleId: String,
    val saleId : UUID,
    // var proFormaUpdatedOn: Long? = null,
    // var createdOn : Long?=null,
    var recordedOn:Long?=null,
    var status: String,
    var totalNoOfBoxes: Int,
    private val participants: List<PublicKey>
) : ContractState {

    override fun getParticipants(): List<PublicKey> = participants

}