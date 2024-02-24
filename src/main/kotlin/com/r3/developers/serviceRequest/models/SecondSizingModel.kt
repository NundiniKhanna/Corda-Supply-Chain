package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class SecondSizingModel(
    val secondSizingResults: String?,
    var secondSizingDoneOn: Long?
)