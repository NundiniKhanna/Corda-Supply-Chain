package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class ShippingDetailsModel(
    val shippingDetailWayModel: ShippingDetailWay?,
    val ratePerKg: Float?,
    var shipmentDoneOn: Long?
)

@CordaSerializable
enum class ShippingDetailWay {
    AIR_SHIPPING,
    WATER_SHIPPING
}
