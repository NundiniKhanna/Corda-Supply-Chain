package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import net.corda.v5.crypto.SecureHash
import java.util.*

@CordaSerializable
data class ResponseModel1(

    val transactionId: String?,
    val requestId: UUID?,
    val error : String?

)