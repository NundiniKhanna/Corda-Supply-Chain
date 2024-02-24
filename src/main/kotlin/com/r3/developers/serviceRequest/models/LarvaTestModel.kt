package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class LarvaTestModel
    (

    var larvaTestResult: LarvaTestResult,
    var rejectionReason: String?,
    var larvaTestDoneOn: Long?
)


@CordaSerializable
enum class LarvaTestResult {
    Pass,
    Failed
}
