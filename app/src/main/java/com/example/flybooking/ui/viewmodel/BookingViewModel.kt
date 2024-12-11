package com.example.flybooking.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.firebase.FirestoreService
import com.example.flybooking.model.Booking
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class BookingState {
    object Loading : BookingState()
    data class Success(val message: String) : BookingState()
    data class Error(val message: String) : BookingState()
    data class BookingDetails(val booking: Booking) : BookingState()
}

class BookingViewModel(
    private val firestoreService: FirestoreService
) : ViewModel() {

    private val _bookingState = MutableLiveData<BookingState>(BookingState.Loading)
    val bookingState: LiveData<BookingState> = _bookingState

    private val _bookingDetailsStateFlow = MutableStateFlow<Booking?>(null)
    val bookingDetailsStateFlow: StateFlow<Booking?> get() = _bookingDetailsStateFlow

    fun createBooking(booking: Booking) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                val bookingId = firestoreService.createBooking(booking)
                _bookingState.value = BookingState.Success("Booking created with ID: $bookingId")
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error creating booking: ${e.message}")
                Log.e("BookingViewModel", "Error creating booking", e)
            }
        }
    }

    fun readBooking(bookingId: String) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                val booking = firestoreService.readBooking(bookingId)
                if (booking != null) {
                    _bookingState.value = BookingState.BookingDetails(booking)
                    _bookingDetailsStateFlow.value = booking
                } else {
                    _bookingState.value = BookingState.Error("Booking not found")
                }
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error fetching booking: ${e.message}")
                Log.e("BookingViewModel", "Error fetching booking", e)
            }
        }
    }

    fun updateBooking(booking: Booking) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                firestoreService.updateBooking(booking)
                _bookingState.value = BookingState.Success("Booking updated successfully")
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error updating booking: ${e.message}")
                Log.e("BookingViewModel", "Error updating booking", e)
            }
        }
    }

    fun deleteBooking(bookingId: String) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                firestoreService.deleteBooking(bookingId)
                _bookingState.value = BookingState.Success("Booking deleted successfully")
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error deleting booking: ${e.message}")
                Log.e("BookingViewModel", "Error deleting booking", e)
            }
        }
    }
}