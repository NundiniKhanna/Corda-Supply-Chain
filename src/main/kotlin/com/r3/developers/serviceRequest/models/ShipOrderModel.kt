package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class ShipOrderModel(
var dateAndTimeOfLoading: Long?,
val totalNoofBoxesLoaded: Int,
val sellCostOfTransportation: Float,
val buyersLocation: String,
val shipOrderCurrency: String,
val saleId: UUID
)