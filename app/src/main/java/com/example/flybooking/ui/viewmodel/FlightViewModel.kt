package com.example.flybooking.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.response.FlightOffer
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.launch

sealed interface FlightUiState {
    data class Success(
        val offers: List<FlightOffer> = emptyList(),
        val selected: FlightOffer? = null
    ) : FlightUiState

    object Error : FlightUiState

    object Loading : FlightUiState
}

class FlightViewModel(
    private val repository: Repository
) : ViewModel() {
    var flightUiState: FlightUiState by mutableStateOf(FlightUiState.Success())
        private set

    fun searchFlights(
        departure: String,
        destination: String,
        departureDate: String,
        returnDate: String,
        adults: Int,
        children: Int
    ) {
        viewModelScope.launch {
            flightUiState = FlightUiState.Loading
            flightUiState = try {
                val offers = repository.searchFlights(
                    departure = departure,
                    destination = destination,
                    departureDate = departureDate,
                    returnDate = returnDate,
                    adults = adults,
                    children = children
                )!!.data
                FlightUiState.Success(offers = offers, selected = offers.firstOrNull())
            } catch (e: Exception) {
                FlightUiState.Error
            }
        }
    }

    fun selectOffer(offer: FlightOffer) {
        flightUiState = (flightUiState as? FlightUiState.Success)?.copy(selected = offer)
            ?: FlightUiState.Success(selected = offer)
    }
}

//data class FlightUiState(
//    val offers: List<FlightOffer> = emptyList(),
//    val isLoading: Boolean = false,
//    val error: Boolean = false
//)