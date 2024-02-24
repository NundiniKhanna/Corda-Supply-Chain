package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class Wire(
    val accountNumber: String,
    val bankName: String,
    val ifsc: String,
    val phoneNumber: String
)