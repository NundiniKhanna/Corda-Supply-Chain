package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class BatchQualityInspectionModel (
    val fruitsAccepted: Int? = null,
    val fruitsRejected: Int? = null,
    val qualityInspectionResults: String? = null,
    var qualityInspectionDoneOn: Long?
)