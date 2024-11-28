package com.example.flybooking.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.TransferInfo
import com.example.flybooking.model.response.amadeus.Transfer
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

data class TransferObject(
    val info: TransferInfo,
    val result: Transfer
)

sealed interface TransferUiState {
    data object Loading : TransferUiState
    data class Success(val transfers: List<TransferObject>) : TransferUiState
    data object Error : TransferUiState
}

class TransferViewModel(
    val repository: Repository
) : ViewModel() {
    var transferUiState: TransferUiState by mutableStateOf(TransferUiState.Loading)
        private set

    fun searchTransfers(
        inputs: List<TransferInfo>,
        countryCode: String
    ) {
        transferUiState = TransferUiState.Loading
        viewModelScope.launch {
            try {
                val results = inputs.map { input ->
                    async {
                        try {
                            val transfers = repository.getTransferOffers(
                                startGeoCode = input.from.geoCode,
                                endGeoCode = input.to.geoCode,
                                countryCode = countryCode,
                                startDateTime = input.date,
                                passengers = 2
                            )?.let { transferList ->
                                transferList
                                    .subList(1, transferList.size)
                                    .filter { transfer ->
                                        val hasValidImageUrl = transfer.vehicle.imageURL?.isNotBlank() == true
                                        val hasPaymentMethods = transfer.methodsOfPaymentAccepted?.isNotEmpty() == true
                                        Log.d(
                                            "FilterDebug",
                                            "ID: ${transfer.id}, ValidImageUrl: $hasValidImageUrl, PaymentMethods: $hasPaymentMethods"
                                        )
                                        hasValidImageUrl && hasPaymentMethods
                                    }
                                    .filter { it.vehicle.imageURL!!.startsWith("https://") }
                                    .sortedBy { it.converted.monetaryAmount.toDouble() }
                            }
                            transfers
                        } catch (e: Exception) {
                            Log.e("TransferViewModel", "Error for input $input: $e")
                            null
                        }
                    }
                }.awaitAll()
                val combinedResults = results.mapIndexedNotNull { index, transferList ->
                    transferList?.getOrNull(index)?.let { TransferObject(inputs[index], it) }
                }

                if (combinedResults.isNotEmpty()) {
                    transferUiState = TransferUiState.Success(combinedResults)
                    Log.d("TransferViewModel", "Combined Transfers: ${combinedResults.size}")
                } else {
                    transferUiState = TransferUiState.Error
                    Log.d("TransferViewModel", "No transfers found.")
                }
            } catch (e: Exception) {
                Log.e("TransferViewModel", "Global Error: $e")
                transferUiState = TransferUiState.Error
            }
        }
    }

}