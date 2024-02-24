package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class FruitWashingModel(
    val waterTemperature: Float?,
    val phLevel: Float?,
    val chlorineLevel: Float?,
    var fruitWashingDoneOn: Long?
)