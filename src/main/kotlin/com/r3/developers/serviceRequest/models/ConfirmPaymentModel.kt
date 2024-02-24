package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class ConfirmPaymentModel(
var confirmPaymentDate: Long?,
val netReceivables: Float,
val paymentNetReceivablesCurrency: String,
val wasFactored: Boolean,
val amountFactored: Float?,
val factoringEntity: String?,
val factoringAgent: String?,
val factoringContactDetails: String?,
val factoringCharges: Float?,
val escrowAccountNo: String?,
val brokerBankTransactionCurrency: String?,
val brokerBankTransactionFee: Float?,
val netPayable: Float? = null,
val saleId: UUID
)
