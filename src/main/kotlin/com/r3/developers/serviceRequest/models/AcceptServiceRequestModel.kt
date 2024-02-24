package com.r3.developers.serviceRequest.models



import net.corda.v5.base.annotations.CordaSerializable
import java.util.UUID

@CordaSerializable
data class AcceptServiceRequestModel(

    var assignedOn: Long?,
    var collectorName: String,
    var collectedOn: Long,
    var requestId: UUID
   // var batchId: String
)