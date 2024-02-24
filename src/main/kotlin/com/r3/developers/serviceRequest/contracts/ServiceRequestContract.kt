package com.r3.developers.serviceRequest.contracts


import com.r3.developers.serviceRequest.models.LarvaTestResult
import com.r3.developers.serviceRequest.models.Step
import com.r3.developers.serviceRequest.states.ServiceRequestState
import net.corda.v5.base.annotations.CordaSerializable
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.ledger.utxo.Command
import net.corda.v5.ledger.utxo.Contract
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction

@CordaSerializable
class ServiceRequestContract : Contract {

    override fun verify(transaction: UtxoLedgerTransaction) {
        val command = transaction.commands.singleOrNull()
            ?: throw CordaRuntimeException("Requires a single command.")

        val outputStates = transaction.outputContractStates.filterIsInstance(ServiceRequestState::class.java)

        when (command) {
            is Create -> verifyCreate(transaction, outputStates)
            is Accept -> verifyAccept(transaction, outputStates)
            is EnrouteToProducer ->verifyEnrouteToProducer(transaction, outputStates)
            is ArrivedOnProducerFarm ->verifyArrivedOnProducerFarm(transaction, outputStates)
            is EnrouteToPackHouse ->verifyEnrouteToPackHouse(transaction, outputStates)
            is ArrivedAtPackHouse -> verifyArrivedAtPackHouse(transaction, outputStates)
            is FruitFlow -> verifyFruitFlow(transaction,outputStates)
            else -> throw CordaRuntimeException("Command not allowed.")
        }
    }

    private fun verifyCreate(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
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

    private fun verifyAccept(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "created")
        {
           "Service Request can be accpeted"
       }

        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyEnrouteToProducer(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "Accepted")
        {
            "Service Request cannot be enrouted-to-producer if is not accepted"
        }

        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyArrivedOnProducerFarm(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "enrouted-to-producer")
        {
            "Service Request cannot be arrived-on-producer-farm if is not enrouted to producer "
        }

        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyEnrouteToPackHouse(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "Arrive-on-producer-farm")
        {
            "Service Request cannot be enrouted to pack house if is not arrived on producer farm"
        }

        // Add more custom contract rules for the Update command if needed.
    }
    private fun verifyArrivedAtPackHouse(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
        require(input.status == "Enroute-to-pack-house")
        {
            "Service Request cannot be arrived at pack house if is not enrouted to pack house"
        }

        // Add more custom contract rules for the Update command if needed.
    }

    private fun verifyFruitFlow(transaction: UtxoLedgerTransaction, outputStates: List<ServiceRequestState>) {
        require(transaction.inputContractStates.size == 1) {
            "Update command should have one and only one input state."
        }
        require(outputStates.size == 1) {
            "Update command should have one and only one output state."
        }

        val input = transaction.inputContractStates.single() as ServiceRequestState
        val output = outputStates.single()

        require(input.requestId == output.requestId) {
            "Service Request ID cannot be changed for Update command."
        }
//        require(input.status == "Arrive-at-pack-house")
//        {
//            "Service Request cannot be started  fruit flow presented if is not accepted"
//        }
        val nextStep = output.fruitFlowModel?.step
        if (nextStep != null) {
        when (nextStep) {
            Step.LarvaTestModel -> {
                require(input.status == "Arrive-at-pack-house")
                {
                    "Service Request cannot be larva test model if is not arrived at pack house"
                }
                if(output.fruitFlowModel?.larvaTest?.larvaTestResult == LarvaTestResult.Failed)
                {
                    throw CordaRuntimeException("Larva test failed. Cannot proceed with the fruit flow.")
                }

                // Add specific validation logic for LarvaTestModel if needed
            }
            Step.GradingModel -> {
                require(input.status == "Larva-Test-Done")
                {
                    "Service Request cannot be grading model if larva test is not done"
                }

                // Add specific validation logic for GradingModel if needed
            }
            Step.FruitWashingModel -> {
                require(input.status == "Grading-Done")
                {
                    "Service Request cannot be fruit washing model if grading model is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.QualityInspectionModel -> {
                require(input.status == "Fruit-Washing-Done")
                {
                    "Service Request cannot be quality inspection model if fruit washing is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.SecondSizingModel -> {
                require(input.status == "Quality-Inspection-Done")
                {
                    "Service Request cannot be second sizing model  if quality inspection is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.ShippingDetailsModel -> {
                require(input.status == "Second-Sizing-Done")
                {
                    "Service Request cannot be shipping details model if second sizing is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.TempMeasurementModel -> {
                require(input.status == "Shipping-Details-Done")
                {
                    "Service Request cannot be Temperature measurement model if shipping details model is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.TransportDetailsModel -> {
                require(input.status == "Temp-Measurement-Done")
                {
                    "Service Request cannot be transport details model  if temperature measurement is not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            Step.CartonFillingAndPalletizingModel -> {
                require(input.status == "Transport-Details-Done")
                {
                    "Service Request cannot be Carton filling and palletizing  if transport details are not done"
                }

                // Add specific validation logic for FruitWashingModel if needed
            }

            // Add more cases for other steps as needed
            else -> {
                // Handle unsupported or invalid steps
                throw CordaRuntimeException("Invalid or unsupported fruit flow step.")
            }
        }
        } else {
            // Handle the case where nextStep is null, if necessary
            throw CordaRuntimeException("Invalid or null fruit flow step.")
        }

        // Add more custom contract rules for the Update command if needed.
    }

    class Create : Command
    class Accept : Command

    class EnrouteToProducer : Command

    class  ArrivedOnProducerFarm : Command

    class EnrouteToPackHouse : Command
    class ArrivedAtPackHouse : Command

    class FruitFlow : Command
}
