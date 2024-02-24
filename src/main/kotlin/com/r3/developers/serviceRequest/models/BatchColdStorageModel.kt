package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class BatchColdStorageModel (
    val temperature: Float,
    val phLevel: Float,
    val ethyleneLevel: Float,
    val co2Level: Float,
    var coldStorageInTimestamp: Long?
)