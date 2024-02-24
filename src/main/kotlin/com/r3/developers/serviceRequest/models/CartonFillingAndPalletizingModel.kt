package com.r3.developers.serviceRequest.models
import net.corda.v5.base.annotations.CordaSerializable

@CordaSerializable
data class CartonFillingAndPalletizingModel(
    val startQRCodeBoxes: String?,
    val endQRCodeBoxes: String?,
    val startQRCodeFruits: String?,
    val endQRCodeFruits: String?,
    val totalBoxes: Int?,
    val totalFruits: Int?,
    val boxSize: Int?,
    val avgWeight: Float?,
    var cartonFillingAndPalletizingDoneOn: Long?
)