package com.example.flybooking.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.response.Hotel
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HotelsUiState {
    data class Success(val hotels: List<Hotel> = emptyList()) : HotelsUiState
    object Error : HotelsUiState
    object Loading : HotelsUiState
}

class HotelsViewModel(private val repository: Repository) : ViewModel() {
    private val _hotelsUiState = MutableStateFlow<HotelsUiState>(HotelsUiState.Loading)
    val hotelsUiState: StateFlow<HotelsUiState> = _hotelsUiState.asStateFlow()

    fun searchHotels(cityCode: String) {
        viewModelScope.launch {
            _hotelsUiState.value = HotelsUiState.Loading
            _hotelsUiState.value = try {
                val hotels = repository.getHotelsByCity(cityCode)
                if (hotels.isNullOrEmpty()) HotelsUiState.Error else HotelsUiState.Success(hotels)
            } catch (e: Exception) {
                HotelsUiState.Error
            }
        }
    }
}

