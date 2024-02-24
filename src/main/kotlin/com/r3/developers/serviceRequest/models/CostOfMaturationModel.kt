package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class CostOfMaturationModel (
    val costOfMaturation: Float,
    val currency: String,
    var costOfMaturationTimestamp: Long?
)