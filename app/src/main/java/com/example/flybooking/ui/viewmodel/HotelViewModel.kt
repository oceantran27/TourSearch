package com.example.flybooking.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.City
import com.example.flybooking.model.TransferLocInfo
import com.example.flybooking.model.response.amadeus.GeoCode
import com.example.flybooking.model.response.tripadvisor.DetailsResponse
import com.example.flybooking.model.response.tripadvisor.Photo
import com.example.flybooking.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.lang.Double.parseDouble

@Serializable
data class HotelObject(
    val cityName: String = "",
    val cityGeoCode: GeoCode = GeoCode(0.0, 0.0),
    val details: DetailsResponse ?= null,
    val photos: List<Photo> = emptyList()
) {
    constructor(): this("", GeoCode(0.0, 0.0), null, emptyList())
    fun toTransferLoc() = TransferLocInfo(
        addressLine = details?.name.toString(),
        geoCode = GeoCode(
            latitude = parseDouble(details?.latitude ?: "0.0"),
            longitude = parseDouble(details?.longitude ?: "0.0")
        )
    )
}

sealed interface HotelUiState {
    data object Loading : HotelUiState
    data class Success(
        val hotelList: List<HotelObject>,
        val selected: HotelObject ?= null
    ) : HotelUiState
    data object Error : HotelUiState
}

class HotelViewModel(
    private val repository: Repository
) : ViewModel() {
    var hotelUiState: HotelUiState by mutableStateOf(HotelUiState.Loading)
        private set

    fun searchHotels(destination: City) {
        hotelUiState = HotelUiState.Loading
        viewModelScope.launch {
            try {
                val cityData = repository.getCityData(destination)!!
                val nearbyIds = repository.getNearbyIds(cityData)!!.subList(4, 5)
                val hotelList: List<HotelObject> = nearbyIds.map { id ->
                    coroutineScope {
                        val detailsDeferred = async { repository.getTripAdvisorDetails(id) }
                        val photosDeferred = async { repository.getTripAdvisorPhotos(id) }
                        val details = detailsDeferred.await()
                        val photos = photosDeferred.await()
                        if (details == null) {
                            Log.e("HotelViewModel", "Error retrieving details")
                            return@coroutineScope HotelObject(destination.name, cityData.geoCode)
                        }
                        if (photos == null) {
                            Log.e("HotelViewModel", "Error retrieving photos")
                            return@coroutineScope HotelObject(destination.name, cityData.geoCode)
                        }
                        HotelObject(destination.name, cityData.geoCode, details, photos)
                    }
                }
                // Remove hotels with not enough information
                hotelList.filter { it.details != null || it.photos.isNotEmpty() }
                hotelUiState = HotelUiState.Success(hotelList, hotelList.first())
            } catch (e: Exception) {
                Log.e("HotelViewModel", "Error retrieving hotels: $e")
                hotelUiState = HotelUiState.Error
            }
        }
    }
}