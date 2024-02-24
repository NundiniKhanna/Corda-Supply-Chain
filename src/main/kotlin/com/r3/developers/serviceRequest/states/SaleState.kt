package com.r3.developers.serviceRequest.states



import com.r3.developers.serviceRequest.contracts.ServiceRequestContract
import com.r3.developers.serviceRequest.contracts.SaleContract
import com.r3.developers.serviceRequest.models.*
//import com.r3.developers.serviceRequest.models.GetServiceRequestBody
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey

import java.util.*

@BelongsToContract(SaleContract::class)
data class SaleState (


    val batchIDList: MutableList<UUID>,

    val saleId: UUID=UUID.randomUUID(),
    //var confirmPaymentDate:  Long?=null,
   // var saleTransactionDate: Long?=null,
    val harvester: PublicKey,
    val lsp: PublicKey,
    val collector: PublicKey,
    val broker: PublicKey,
    val creationData: NewSaleModel? = null,
    var shipOrderData: ShipOrderModel? = null,
    var unloadingAtBuyerData: UnloadingAtBuyerModel? = null,
    var salesInvoiceData: SalesInvoiceModel? = null,
    var confirmPaymentData: ConfirmPaymentModel? = null,
    var updatedOn: Long,
    var status: String,

    private val participants: List<PublicKey>
) : ContractState {

    override fun getParticipants(): List<PublicKey> = participants

}