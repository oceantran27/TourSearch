package com.example.flybooking.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.response.Activity
import com.example.flybooking.model.response.ActivityCard
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.launch

sealed interface ActivitiesUiState {
    data class Success(
        val cards: List<ActivityCard> = emptyList(),
        val totalCost: Double = 0.0,
        val selected: List<Activity> = emptyList()
    ) : ActivitiesUiState
    object Error : ActivitiesUiState
    object Loading : ActivitiesUiState
}


class ActivitiesViewModel(
    private val repository: Repository
) : ViewModel() {
    var activitiesUiState: ActivitiesUiState by mutableStateOf(ActivitiesUiState.Loading)
        private set

    fun searchActivities(destination: String) {
        viewModelScope.launch {
            activitiesUiState = ActivitiesUiState.Loading
            activitiesUiState = try {
                val geocode = repository.getGeocodeFromIATA(destination)
                geocode.let {
                    val activities = repository.getActivities(it!!.latitude, it.longitude)
                    val validActivities = activities?.filter {
                            activity -> activity.isValid()
                    }?.sortedBy { activity -> activity.price?.amount?.toDoubleOrNull() }
                        ?.map { activity -> ActivityCard(activity, false) }?: emptyList()
                    ActivitiesUiState.Success(cards = validActivities)
                }
            } catch (e: Exception) {
                ActivitiesUiState.Error
            }
        }
    }

    fun selectActivity(card: ActivityCard) {
        if (activitiesUiState is ActivitiesUiState.Success) {
            val successState = activitiesUiState as ActivitiesUiState.Success
            val totalCost = successState.totalCost +
                    convertToUSD(
                        price = card.activity.price?.amount?.toDoubleOrNull()!!,
                        currency = card.activity.price.currencyCode ?: "USD"
                    )
            val updatedCards = successState.cards.map {
                if (it.activity == card.activity) ActivityCard(it.activity, true) else it
            }
            activitiesUiState = ActivitiesUiState.Success(
                cards = updatedCards,
                totalCost = totalCost,
                selected = successState.selected + card.activity
            )
        }
    }

    fun deselectActivity(card: ActivityCard) {
        if (activitiesUiState is ActivitiesUiState.Success) {
            val successState = activitiesUiState as ActivitiesUiState.Success
            val totalCost = successState.totalCost +
                    convertToUSD(
                        price = card.activity.price?.amount?.toDoubleOrNull()!!,
                        currency = card.activity.price.currencyCode ?: "USD"
                    )
            val updatedCards = successState.cards.map {
                if (it.activity == card.activity) ActivityCard(it.activity, false) else it
            }
            activitiesUiState = ActivitiesUiState.Success(
                cards = updatedCards,
                totalCost = totalCost,
                selected = successState.selected - card.activity
            )
        }
    }
}


//data class ActivitiesState(
//    val cards: List<ActivityCard> = emptyList(),
//    val selected: List<Activity> = emptyList(),
//    val totalCost: Double = 0.0,
//    val isLoading: Boolean = false,
//    val error: Boolean = false
//)

fun convertToUSD(price: Double, currency: String): Double {
    val exchangeRates = mapOf(
        "EUR" to 1.1,
        "GBP" to 1.3,
        "USD" to 1.0
    )
    return price * (exchangeRates[currency] ?: 1.0)
}
