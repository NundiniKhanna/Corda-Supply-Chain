package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class QualityInspectionModel(
    var acceptedQty: Int?,
    var rejectedQty: Int?,
    var qualityInspectionResult: String?,
    var qualityInspectionDoneOn: Long?
)