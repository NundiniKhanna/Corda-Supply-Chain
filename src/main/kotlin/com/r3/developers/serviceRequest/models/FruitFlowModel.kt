package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class FruitFlowModel(
    val step: Step,

    var larvaTest: LarvaTestModel? = null,
    var tempMeasurement: TempMeasurementModel? = null,
    var qualityInspection: QualityInspectionModel? = null,
    val fruitWashing: FruitWashingModel? = null,
    val grading: GradingModel? = null,
    val secondSizing: SecondSizingModel? = null,
    val cartonFillingAndPalletizing: CartonFillingAndPalletizingModel? = null,
    val shippingDetails: ShippingDetailsModel? = null,
    val transportDetails: TransportDetailsModel? = null,
    val requestId: UUID
 //   val batchId: String
)

@CordaSerializable
enum class Step{
    LarvaTestModel,
    GradingModel,
    FruitWashingModel,
    QualityInspectionModel,
    SecondSizingModel,
    ShippingDetailsModel,
    TempMeasurementModel,
    TransportDetailsModel,
    CartonFillingAndPalletizingModel,
}


