package com.r3.developers.serviceRequest.contracts


import com.r3.developers.serviceRequest.models.LarvaTestResult
import com.r3.developers.serviceRequest.models.Step
import com.r3.developers.serviceRequest.models.Step2
import com.r3.developers.serviceRequest.states.ServiceRequestState
import com.r3.developers.serviceRequest.states.BatchState
import net.corda.v5.base.annotations.CordaSerializable
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.ledger.utxo.Command
import net.corda.v5.ledger.utxo.Contract
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction

@CordaSerializable
class BatchContract : Contract {


    override fun verify(transaction: UtxoLedgerTransaction) {
        val command = transaction.commands.singleOrNull()
            ?: throw CordaRuntimeException("Requires a single command.")

        val outputStates = transaction.outputContractStates.filterIsInstance(BatchState::class.java)

        when (command) {
            is Create -> verifyCreate(transaction, outputStates)
            is Update -> verifyUpdate(transaction, outputStates)
            is PerformaInvoice -> verifyPerformaInvoice(transaction, outputStates)
            else -> throw CordaRuntimeException("Command not allowed.")
        }
    }

    private fun verifyCreate(transaction: UtxoLedgerTransaction, outputStates: List<BatchState>) {
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

    private fun verifyUpdate(transaction: UtxoLedgerTransaction, outputStates: List<BatchState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as BatchState
        val output = outputStates.single()

        require(input.batchId == output.batchId) {
            "Batch ID cannot be changed for Update command."
        }
      //  require(input.status == "created-batch-request")
//        {
//            "Service Request cannot be fruit flow presented if is not accepted"
//        }
        val nextStep = output.updateBatchModel?.step
        if (nextStep != null) {
            when (nextStep) {
                Step2.ArrivalAndDestinationModel-> {
                    require(input.status == "created-batch-request")
                    {
                        "batch status cannot be arrival and destination if the batch request is not created"
                    }
//                    if(output.updateBatchModel?.arrivalAndDestinationData?.arrivalTimestamp== LarvaTestResult.Failed)
//                    {
//                        throw CordaRuntimeException("Larva test failed. Cannot proceed with the fruit flow.")
//                    }

                    // Add specific validation logic for LarvaTestModel if needed
                }
                Step2.BatchQualityInspectionModel -> {
                    require(input.status == "arrival-and-destination-done")
                    {
                        "Service Request cannot be batch quality inspection model if arrival and destination is not done"
                    }

                    // Add specific validation logic for GradingModel if needed
                }
                Step2.BatchColdStorageModel -> {
                    require(input.status == "batch-quality-inspection-done")
                    {
                        "Service Request cannot be batch cold storage model if batch quality inspection  is not done"
                    }

                    // Add specific validation logic for FruitWashingModel if needed
                }

                Step2.CostOfMaturationModel -> {
                    require(input.status == "batch-cold-storage-done")
                    {
                        "Service Request cannot be cost of maturation model if batch cold storage is not done"
                    }

                    // Add specific validation logic for FruitWashingModel if needed
                }


                // Add more cases for other steps as needed
                else -> {
                    // Handle unsupported or invalid steps
                    throw CordaRuntimeException("Invalid or unsupported update batch step.")
                }
            }
        } else {
            // Handle the case where nextStep is null, if necessary
            throw CordaRuntimeException("Invalid or null update batch step.")
        }

        // Add more custom contract rules for the Update command if needed.
    }


        // Add more custom contract rules for the Update command if needed.


    private fun verifyPerformaInvoice(transaction: UtxoLedgerTransaction, outputStates: List<BatchState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as BatchState
        val output = outputStates.single()

        require(input.batchId == output.batchId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "Updated batch")
//        {
//            "Service Request cannot be fruit flow presented if is not accepted"
//        }


        // Add more custom contract rules for the Update command if needed.
    }


class Create : Command
class Update : Command

class PerformaInvoice : Command
}