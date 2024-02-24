package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable

data class CreateBatchStateModel (

    val serviceRequestIDList: MutableList<UUID>,
   // val batchId: String,
   // val saleId: String,
    //val requestId: String,
    var createdOn: Long?,
    var recordedOn: Long?,
    val totalWeight: Double,
   // val status: String,
    var totalNoOfBoxes: Int,
    var totalNoOfBoxesRemaining: Int?
)