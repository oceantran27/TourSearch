package com.example.flybooking.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.firebase.FirestoreService
import com.example.flybooking.model.Booking
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class BookingState {
    data object Loading : BookingState()
    data object Empty : BookingState()
    data class Success(val message: String) : BookingState()
    data class Error(val message: String) : BookingState()
    data class BookingDetails(val booking: Booking) : BookingState()
}

fun serializeBooking(booking: Booking): String {
    return Json.encodeToString(booking)
}

fun deserializeBooking(json: String): Booking {
    return Json.decodeFromString(json)
}

class BookingViewModel(
    private val firestoreService: FirestoreService,
) : ViewModel() {

    private val _bookingState = MutableLiveData<BookingState>(BookingState.Empty)
    val bookingState: LiveData<BookingState> = _bookingState

    private val _bookingDetailsStateFlow = MutableStateFlow<Booking?>(null)
    val bookingDetailsStateFlow: StateFlow<Booking?> get() = _bookingDetailsStateFlow

    fun dbCreateBooking(booking: Booking) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                val bookingJson = serializeBooking(booking)
                Log.d("BookingViewModel", "Serialized Booking: $bookingJson")

                val bookingId = firestoreService.createBooking(booking)
                Log.d("BookingViewModel", "Booking created with ID: $bookingId")
                _bookingState.value = BookingState.Success("Booking created with ID: $bookingId")
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error creating booking: ${e.message}")
                Log.e("BookingViewModel", "Error creating booking", e)
            }
        }
    }

//    suspend fun dbReadBooking(bookingId: String): Booking? {
//        _bookingState.value = BookingState.Loading
//        var ret: Booking? = null
//        viewModelScope.launch {
//            try {
////                val booking = firestoreService.readBooking(bookingId)
////                if (booking != null) {
////                    _bookingState.value = BookingState.BookingDetails(booking)
////                    _bookingDetailsStateFlow.value = booking
////                } else {
////                    _bookingState.value = BookingState.Error("Booking not found")
////                }
//                ret = firestoreService.readBooking(bookingId)
//            } catch (e: Exception) {
////                _bookingState.value = BookingState.Error("Error fetching booking: ${e.message}")
//                Log.e("BookingViewModel", "Error fetching booking", e)
//            }
//        }
//        return ret
//    }

    suspend fun getAllBooking(ids: List<String>): List<Booking> = coroutineScope {
        try {
            // Chạy các công việc bất đồng bộ và đợi tất cả kết quả
            ids.map { id ->
                async {
                    firestoreService.readBooking(id)
                }
            }.awaitAll().filterNotNull() // Đợi và lọc kết quả
        } catch (e: Exception) {
            Log.e("BookingViewModel", "Error fetching booking", e)
            emptyList() // Trả về danh sách rỗng nếu có lỗi
        }
    }

    fun dbUpdateBooking(booking: Booking) {
        _bookingState.value = BookingState.Loading
        viewModelScope.launch {
            try {
                // Serialize đối tượng Booking thành JSON trước khi gửi lên Firestore
                val bookingJson = serializeBooking(booking)
                Log.d("BookingViewModel", "Serialized Booking for Update: $bookingJson")

                firestoreService.updateBooking(booking)
                _bookingState.value = BookingState.Success("Booking updated successfully")
            } catch (e: Exception) {
                _bookingState.value = BookingState.Error("Error updating booking: ${e.message}")
                Log.e("BookingViewModel", "Error updating booking", e)
            }
        }
    }

    fun dbDeleteBooking(bookingId: String) {
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
