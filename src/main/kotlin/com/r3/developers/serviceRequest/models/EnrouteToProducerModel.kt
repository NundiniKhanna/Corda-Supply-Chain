package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable

data class EnrouteToProducerModel (
    var enrouteToProducerOn: Long?,
    var additionalNotes: String,
    val requestId: UUID
   // val batchId: String
)