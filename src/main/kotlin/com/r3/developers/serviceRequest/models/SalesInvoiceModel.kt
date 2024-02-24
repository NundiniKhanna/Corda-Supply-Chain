package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import java.util.UUID

@CordaSerializable
data class SalesInvoiceModel(
val invoiceNo: String,
var billingDate: Long?,
val brokerName: String,

val buyerName: String,

val noofBoxesPurchased: Int,

val pricePerKg: Float,
val pricePerKgCurrency: String,

val netSales: Float,
val aproximateWeightOfProduct: Float,
val brokerPercent: Float,
val brokerTransportFlatFee: Float,
//val requestId: String,
val saleId: UUID
)