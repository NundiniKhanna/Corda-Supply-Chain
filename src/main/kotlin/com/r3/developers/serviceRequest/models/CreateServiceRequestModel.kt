package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.time.Instant
import java.util.*

@CordaSerializable
data class CreateServiceRequestModel (
    val formerName: String,
    val formerLocation: String,
    val contactNumber: String,
    val quantity: Int,
    val product: String,
    val harvestedOn: Long,
   val createdOn: Long?,
    //val requestId: uui
    //val requestId: UUID = UUID.randomUUID()
   // val batchId: String,
   // val saleId: String
)

