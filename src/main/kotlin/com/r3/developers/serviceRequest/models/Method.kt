package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
enum class Method {
    Cash,
    Phone,
    Wire
}