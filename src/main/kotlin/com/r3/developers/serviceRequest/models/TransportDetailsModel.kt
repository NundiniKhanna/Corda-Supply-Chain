package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class TransportDetailsModel(
    val transportTemperature: Float?,
    val destinationTransport: String?,
    val transportConditions: String?,
    val transportCost: Float?,
    val transportCurrency: String?,
    var departureDateTimeTransport: Long? = null
)