package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class UnloadingAtBuyerModel(
var dateAndTimeOfUnLoading: Long?,
val totalNoofBoxesUnLoaded: Int,
val saleId: UUID
)