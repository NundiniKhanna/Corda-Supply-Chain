package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.UUID

@CordaSerializable

data class ArrivedAtPackHouseModel(
    var arrivedAtPackHouseOn: Long?,
    var additionalNotes: String,
    val requestId: UUID
    //val batchId: String
)