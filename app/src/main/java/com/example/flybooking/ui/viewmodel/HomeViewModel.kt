package com.example.flybooking.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flybooking.R
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

const val ONE_DAY_TIME: Long = 86400000L
const val ONE_WEEK_TIME: Long = 7 * ONE_DAY_TIME

class HomeViewModel(
        private val repository: Repository,
) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    // StateFlow để giữ thông báo lỗi
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun checkValidForm(departure: String, destination: String, moneyAmount: Double): Boolean {
        return departure.isNotBlank() && destination.isNotBlank() && moneyAmount > 0.0
    }

    // Tăng số lượng hành khách người lớn
    fun increaseAdultCount() {
        _homeUiState.update { state ->
            state.copy(passengerAdultCount = state.passengerAdultCount + 1)
        }
    }

    // Giảm số lượng hành khách người lớn
    fun decreaseAdultCount() {
        _homeUiState.update { state ->
            val newCount = (state.passengerAdultCount - 1).coerceAtLeast(1)
            state.copy(passengerAdultCount = newCount)
        }
    }

    // Tăng số lượng hành khách trẻ em
    fun increaseChildCount() {
        _homeUiState.update { state ->
            state.copy(passengerChildCount = state.passengerChildCount + 1)
        }
    }

    // Giảm số lượng hành khách trẻ em
    fun decreaseChildCount() {
        _homeUiState.update { state ->
            val newCount = (state.passengerChildCount - 1).coerceAtLeast(0)
            state.copy(passengerChildCount = newCount)
        }
    }

    fun updateDepartureDate(newDate: Long) {
        val today = System.currentTimeMillis()

        // Điều kiện: Ngày đi không được nhỏ hơn hôm nay
        if (newDate < today) {
            _errorMessage.value = "Departure date cannot be earlier than today"
            _homeUiState.update { state ->
                state.copy(departureDate = today)
            }
        } else {
            _homeUiState.update { state ->
                val updatedReturnDate = if (state.returnDate <= newDate) {
                    newDate + ONE_DAY_TIME
                } else {
                    state.returnDate
                }
                state.copy(departureDate = newDate, returnDate = updatedReturnDate)
            }
        }
    }

    fun updateReturnDate(newDate: Long) {
        _homeUiState.update { state ->
            // Điều kiện: Ngày về phải lớn hơn ngày đi ít nhất 1 ngày
            val minimumReturnDate = state.departureDate + ONE_DAY_TIME // Ngày đi + 1 ngày
            if (newDate < minimumReturnDate) {
                _errorMessage.value = "Return date must be at least 1 day after the departure date"
                state.copy(returnDate = minimumReturnDate)
            } else {
                state.copy(returnDate = newDate)
            }
        }
    }

    // Xóa thông báo lỗi sau khi đã hiển thị
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun updateDeparture(city: String) {
        _homeUiState.update { it.copy(
            departure = city,
            isFormValid = checkValidForm(city, it.destination, it.moneyAmount)
        ) }
    }

    fun updateDestination(city: String) {
        _homeUiState.update {
            it.copy(
                destination = city,
                isFormValid = checkValidForm(it.departure, city, it.moneyAmount)
            )
        }
    }

    fun updateMoney(money: String) {
        var moneyAmount = money.toDoubleOrNull() ?: 0.0
        if (money.isBlank()) {
            moneyAmount = 0.0
        }
        _homeUiState.update {
            it.copy(
                moneyAmount = moneyAmount,
                isFormValid = checkValidForm(it.departure, it.destination, moneyAmount)
            )
        }
    }
}

data class HomeUiState(
    val bannerName: String = "TravelMate",
    val bannerDesc: String = "Discover Your Journey",
    val bannerImageId: Int = R.drawable.profile,
    val departure: String = "",
    val destination: String = "",
    val passengerAdultCount: Int = 1,
    val passengerChildCount: Int = 1,
    val departureDate: Long = System.currentTimeMillis() + ONE_WEEK_TIME,
    val returnDate: Long = departureDate + ONE_WEEK_TIME, // Mặc định là 1 tuần sau
    val moneyAmount: Double = 0.0,
    val isFormValid: Boolean = false
)