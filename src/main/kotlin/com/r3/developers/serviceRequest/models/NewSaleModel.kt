package com.r3.developers.serviceRequest.models

import net.corda.v5.base.annotations.CordaSerializable
import java.util.*

@CordaSerializable
data class NewSaleModel(
   // val serviceRequestIDList: MutableList<String>,
    val batchIDList: MutableList<UUID>,

    //val saleId:
 //   val batchId: String,
    val sellBuyerName: String,
    val sellBuyerAddress: String,
    val sellBuyerContactDetails: String,
    val sellBuyerEmailAddress: String,
    var saleTransactionDate: Long?,
    val totalNoOfBoxesSold: Int,
    val salesPricePerKg: Float,
    val salesWeightPerBox: Float,
    val salesCurrency: String,
    val salesBrokerMargin: Float,
    val avgWeight: Float? = null,
    val ratePerKg: Float? = null

)