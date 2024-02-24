package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class ResponseModel2 (

    val transactionId : String?,
    val batchId : UUID?,
    val error : String?




)