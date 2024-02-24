package com.r3.developers.serviceRequest.models
import com.r3.developers.serviceRequest.models.PaymentMethod
import com.r3.developers.serviceRequest.models.Cash
import com.r3.developers.serviceRequest.models.Wire
import com.r3.developers.serviceRequest.models.Method
import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class ArrivedOnProducerFarmModel(
    var acceptedQuantity: Int,
    var rejectedQuantity: Int,
    var rejectionReason: String,
    var advanceGiven: Int,
    var advanceGivenCurrency: String,
    var advanceGivenToLsp: Int,
    var advanceGivenCurrencyToLsp: String,
    var paymentMethod: PaymentMethod,
    var additionalNotes: String,
    var requestId:  UUID
   // var status: String
   // var batchId:String
)
@CordaSerializable
data class PaymentMethod(
    val method: Method,
    val cash: Cash? = null,
    val wire: Wire? = null,
    val phone: Phone? =null
)