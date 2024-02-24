package com.r3.developers.serviceRequest.contracts


import com.r3.developers.serviceRequest.states.ServiceRequestState
import com.r3.developers.serviceRequest.states.SaleState
import net.corda.v5.base.annotations.CordaSerializable
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.ledger.utxo.Command
import net.corda.v5.ledger.utxo.Contract
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction

@CordaSerializable
class SaleContract : Contract {


    override fun verify(transaction: UtxoLedgerTransaction) {
        val command = transaction.commands.singleOrNull()
            ?: throw CordaRuntimeException("Requires a single command.")

        val outputStates = transaction.outputContractStates.filterIsInstance(SaleState::class.java)

        when (command) {
            is Create -> verifyCreate(transaction, outputStates)
            is Update -> verifyUpdate(transaction, outputStates)
            is UnloadAtBuyerData ->verifyUnloadAtBuyerData(transaction,outputStates)
            is SalesInvoiceData -> verifySalesInvoiceData(transaction, outputStates)
            is ConfirmPaymentData -> verifyConfirmPaymentData(transaction, outputStates)
            else -> throw CordaRuntimeException("Command not allowed.")
        }
    }

    private fun verifyCreate(transaction: UtxoLedgerTransaction, outputStates: List<SaleState>) {
        require(transaction.inputContractStates.isEmpty()) {
            "Create command should have no input states."
        }
        require(outputStates.size == 1) {
            "Create command should have one and only one output state."
        }
        val output = outputStates.single()

//        require(output.formerName.isNotBlank()) {
//            "The former name must not be blank."
//        }

        // Add more validation for other fields as needed.
    }

    private fun verifyUpdate(transaction: UtxoLedgerTransaction, outputStates: List<SaleState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as SaleState
        val output = outputStates.single()

        require(input.saleId == output.saleId) {
            "Service Request ID cannot be changed for Update command."
        }

        require(input.status == "created-sale-request")



        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyUnloadAtBuyerData(transaction: UtxoLedgerTransaction, outputStates: List<SaleState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as SaleState
        val output = outputStates.single()

        require(input.saleId == output.saleId) {
            "Service Request ID cannot be changed for Update command."
        }

        require(input.status == "ship-order-data")



        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifySalesInvoiceData(transaction: UtxoLedgerTransaction, outputStates: List<SaleState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as SaleState
        val output = outputStates.single()

        require(input.saleId == output.saleId) {
            "Service Request ID cannot be changed for Update command."
        }

        require(input.status == "unload-at-buyer-data")



        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyConfirmPaymentData(transaction: UtxoLedgerTransaction, outputStates: List<SaleState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as SaleState
        val output = outputStates.single()

        require(input.saleId == output.saleId) {
            "Service Request ID cannot be changed for Update command."
        }

        require(input.status == "sales-invoice-data")



        // Add more custom contract rules for the Update command if needed.
    }

    class Create : Command
    class Update : Command

    class UnloadAtBuyerData : Command

    class SalesInvoiceData : Command
    class ConfirmPaymentData : Command
}
