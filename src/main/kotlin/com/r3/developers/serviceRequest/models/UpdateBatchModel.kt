package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class UpdateBatchModel (
    val batchId: UUID,
    val step: Step2,
    var arrivalAndDestinationData: ArrivalAndDestinationModel? = null,
    val qualityInspectionData: BatchQualityInspectionModel? = null,
    val coldStorageData: BatchColdStorageModel? = null,
    val costOfMaturationData: CostOfMaturationModel? = null,
)
@CordaSerializable
enum class Step2{
    ArrivalAndDestinationModel,
    BatchQualityInspectionModel,
    BatchColdStorageModel,
    CostOfMaturationModel


}