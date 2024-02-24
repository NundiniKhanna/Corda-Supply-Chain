package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class GradingModel(
    val gradingResults: String?,
    val weightOfRemovedFruit: Int?,
    var gradingDoneOn: Long?
)