package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class Cash(
    val phoneNumber: String,
    val currency: String
)
