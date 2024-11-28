package com.example.flybooking.repository

import android.util.Log
import com.example.flybooking.BuildConfig
import com.example.flybooking.model.City
import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.CityData
import com.example.flybooking.model.response.amadeus.FlightSearchResponse
import com.example.flybooking.model.response.amadeus.GeoCode
import com.example.flybooking.model.response.amadeus.Transfer
import com.example.flybooking.model.response.tripadvisor.DetailsResponse
import com.example.flybooking.model.response.tripadvisor.Photo
import com.example.flybooking.network.AmadeusApiService
import com.example.flybooking.network.TransferRequest
import com.example.flybooking.network.TripAdvisorApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import retrofit2.Response
import java.util.concurrent.TimeUnit

class Repository(
    private val amadeusApiService: AmadeusApiService,
    private val tripAdvisorApiService: TripAdvisorApiService
) {

    private var accessToken: String? = null
    private var tokenExpirationTime: Long = 0

    private suspend fun getAccessTokenIfNeeded(): String? {
        return withContext(Dispatchers.IO) {
            val currentTime = System.currentTimeMillis()
            if (accessToken.isNullOrEmpty() || currentTime >= tokenExpirationTime) {
                getAccessToken { token, expiresIn, error ->
                    if (token != null && expiresIn != null) {
                        accessToken = token
                        tokenExpirationTime = currentTime + TimeUnit.SECONDS.toMillis(expiresIn)
                    } else {
                        Log.e("AccessToken", "Error retrieving token: $error")
                    }
                }
            }
            accessToken
        }
    }

    private suspend fun getAccessToken(callback: (String?, Long?, String?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = amadeusApiService.getAccessToken("client_credentials", buildAuthHeader())
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token
                    val expiresIn = response.body()?.expires_in
                    callback(accessToken, expiresIn, if (accessToken != null) null else "Access token is null")
                } else {
                    callback(null, null, "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null, null, e.message)
            }
        }
    }

    suspend fun searchFlights(
        departure: String,
        destination: String,
        departureDate: String,
        returnDate: String,
        adults: Int,
        children: Int
    ): FlightSearchResponse? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null

            val response = retryWithDelay {
                amadeusApiService.searchFlights(
                    authorization = "Bearer $accessToken",
                    origin = departure,
                    destination = destination,
                    departureDate = departureDate,
                    returnDate = returnDate,
                    adults = adults,
                    children = children
                )
            }

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        }
    }

    private fun buildAuthHeader(): String {
        val apiKey = BuildConfig.API_KEY
        val apiSecret = BuildConfig.API_SECRET
        return "Basic " + android.util.Base64.encodeToString(
            "$apiKey:$apiSecret".toByteArray(),
            android.util.Base64.NO_WRAP
        )
    }

    suspend fun getGeocodeFromIATA(keyword: String): GeoCode? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null
            val response = retryWithDelay {
                amadeusApiService.getLocation(
                    authorization = "Bearer $accessToken",
                    keyword = keyword
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data?.firstOrNull()?.geoCode
            } else null
        }
    }

    suspend fun getActivities(latitude: Double, longitude: Double): List<Activity>? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null
            val response = retryWithDelay {
                amadeusApiService.getActivities(
                    authorization = "Bearer $accessToken",
                    latitude = latitude,
                    longitude = longitude
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data
            } else {
                null
            }
        }
    }

    suspend fun getTransferOffers(
        startGeoCode: GeoCode,
        endGeoCode: GeoCode,
        countryCode: String,
        startDateTime: String,
        passengers: Int
    ): List<Transfer>? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null
            val transferRequest = TransferRequest(
                startGeoCode = startGeoCode.toString(),
                startCountryCode = countryCode,
                endGeoCode = endGeoCode.toString(),
                endCountryCode = countryCode,
                startDateTime = startDateTime,
                passengers = passengers
            )
            val response = amadeusApiService.searchTransferOffers(
                authorization = "Bearer $accessToken",
                transferRequest = RequestBody.create(
                    "text/plain".toMediaType(),
                    transferRequest.toString()
                )
            )
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        }
    }


    suspend fun getCityData(
        city: City
    ): CityData? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null
            val response = retryWithDelay {
                amadeusApiService.getCityData(
                    authorization = "Bearer $accessToken",
                    keyword = city.name,
                    countryCode = city.countryCode
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data?.firstOrNull()
            } else {
                null
            }
        }
    }

    suspend fun getNearbyIds(
        cityData: CityData
    ): List<String>? {
        return withContext(Dispatchers.IO) {
            val response = retryWithDelay {
                tripAdvisorApiService.getNearbyIds(
                    latLong = cityData.geoCode.toString()
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data?.map { it.locationId }
            } else {
                null
            }
        }
    }

    suspend fun getTripAdvisorDetails(
        locationId: String
    ): DetailsResponse? {
        return withContext(Dispatchers.IO) {
            val response = retryWithDelay {
                tripAdvisorApiService.getDetails(
                    locationId = locationId
                )
            }
            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun getTripAdvisorPhotos(
        locationId: String
    ): List<Photo>? {
        return withContext(Dispatchers.IO) {
            val response = retryWithDelay {
                tripAdvisorApiService.getPhotos(
                    locationId = locationId
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data
            } else {
                null
            }
        }
    }
}

suspend fun <T> retryWithDelay(
    maxRetries: Int = 3,
    delayMs: Long = 0L,
    apiCall: suspend () -> Response<T>
): Response<T>? {
    var currentAttempt = 0
    var lastError: Response<T>? = null

    while (currentAttempt < maxRetries) {
        try {
            val response = apiCall()

            if (response.isSuccessful) {
                return response
            }

            if (response.code() == 500) {
                lastError = response
                currentAttempt++
                delay(delayMs)
            } else {
                return response
            }
        } catch (e: Exception) {
            Log.e("RetryWithDelay", "Error: ${e.message}")
            currentAttempt++
            delay(delayMs)
        }
    }

    return lastError
}