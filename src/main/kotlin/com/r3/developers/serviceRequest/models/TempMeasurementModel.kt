package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class TempMeasurementModel(
    var ambientTemp: Float?,
    var internalFruitTemp: Float?,
    var isTempBreached: Boolean?,
    var tempBreachCount: Float?,
    var penality: Float?,
    var tempMeasurementDoneOn: Long? = null
)