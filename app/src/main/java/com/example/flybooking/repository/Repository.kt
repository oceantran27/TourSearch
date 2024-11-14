package com.example.flybooking.repository

import android.util.Log
import com.example.flybooking.BuildConfig
import com.example.flybooking.model.response.Activity
import com.example.flybooking.model.response.FlightSearchResponse
import com.example.flybooking.model.response.GeoCode
import com.example.flybooking.model.response.Hotel
import com.example.flybooking.model.response.HotelsResponse
import com.example.flybooking.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.TimeUnit

class Repository(private val apiService: ApiService) {

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

    suspend fun getAccessToken(callback: (String?, Long?, String?) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAccessToken("client_credentials", buildAuthHeader())
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
                apiService.searchFlights(
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
                apiService.getLocation(
                    authorization = "Bearer $accessToken",
                    keyword = keyword
                )
            }
            if (response?.isSuccessful == true) {
                response.body()?.data?.firstOrNull()?.geoCode
            } else null
        }
    }

    suspend fun getHotelsByCity(cityCode: String): List<Hotel>? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null

            val response = retryWithDelay {
                apiService.getHotelByCity(
                    authorization = "Bearer $accessToken",
                    cityCode = cityCode
                )
            }

            if (response?.isSuccessful == true) {
                response.body()?.data // Trả về danh sách hotels từ HotelsResponse
            } else {
                null
            }
        }
    }


    suspend fun getActivities(latitude: Double, longitude: Double): List<Activity>? {
        return withContext(Dispatchers.IO) {
            val accessToken = getAccessTokenIfNeeded() ?: return@withContext null
            val response = retryWithDelay {
                apiService.getActivities(
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
}

suspend fun <T> retryWithDelay(
    maxRetries: Int = 3,
    delayMs: Long = 2000L,
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