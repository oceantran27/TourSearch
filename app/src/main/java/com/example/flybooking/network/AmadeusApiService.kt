package com.example.flybooking.network

import android.util.Log
import com.example.flybooking.model.response.amadeus.AccessTokenResponse
import com.example.flybooking.model.response.amadeus.ActivitiesResponse
import com.example.flybooking.model.response.amadeus.CityResponse
import com.example.flybooking.model.response.amadeus.FlightSearchResponse
import com.example.flybooking.model.response.amadeus.HotelsResponse
import com.example.flybooking.model.response.amadeus.LocationResponse
import com.example.flybooking.model.response.amadeus.TransfersResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AmadeusApiService {

    // Lấy access token
    @FormUrlEncoded
    @POST("v1/security/oauth2/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Header("Authorization") authorization: String
    ): Response<AccessTokenResponse>

    // Tìm kiếm chuyến bay
    @GET("v2/shopping/flight-offers")
    suspend fun searchFlights(
        @Header("Authorization") authorization: String,
        @Query("originLocationCode") origin: String,
        @Query("destinationLocationCode") destination: String,
        @Query("departureDate") departureDate: String,
        @Query("returnDate") returnDate: String,
        @Query("adults") adults: Int = 1,
        @Query("children") children: Int = 0,
    ): Response<FlightSearchResponse>

    // Lấy tọa độ địa lý (geocode) dựa trên từ khóa
    @GET("v1/reference-data/locations")
    suspend fun getLocation(
        @Header("Authorization") authorization: String,
        @Query("subType") subType: String = "CITY",
        @Query("keyword") keyword: String
    ): Response<LocationResponse>

    // Lấy danh sách activities dựa trên tọa độ địa lý
    @GET("v1/shopping/activities")
    suspend fun getActivities(
        @Header("Authorization") authorization: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int = 5
    ): Response<ActivitiesResponse>

    // Tìm kiếm phương tiện di chuyển
    @Headers("Content-Type: text/plain")
    @POST("v1/shopping/transfer-offers")
    suspend fun searchTransferOffers(
        @Header("Authorization") authorization: String,
        @Body transferRequest: RequestBody
    ): Response<TransfersResponse>


    // Tìm khách sạn theo city code
    @GET("v1/reference-data/locations/hotels/by-city")
    suspend fun searchHotels(
        @Header("Authorization") authorization: String,
        @Query("cityCode") cityCode: String,
        @Query("radius") radius: Int = 10,
        @Query("radiusUnit") radiusUnit: String = "KM"
    ): Response<HotelsResponse>

    // Tìm kiếm toa độ địa lý dựa trên thành phố
    @GET("v1/reference-data/locations/cities")
    suspend fun getCityData(
        @Header("Authorization") authorization: String,
        @Query("keyword") keyword: String,
        @Query("countryCode") countryCode: String
    ): Response<CityResponse>
}

data class TransferRequest(
    val startGeoCode: String,
    val startAddressLine: String = "start",
    val startCountryCode: String,
    val endGeoCode: String,
    val endAddressLine: String = "end",
    val endCountryCode: String,
    val startDateTime: String,
    val passengers: Int
) {
    override fun toString(): String {
        val res = "{\"startGeoCode\":\"48.85657,2.356067\",\"startAddressLine\":\"start\",\"startCountryCode\":\"FR\",\"endGeoCode\":\"48.85693,2.3412\",\"endAddressLine\":\"end\",\"endCountryCode\":\"FR\",\"startDateTime\":\"2024-12-10T10:29:45\",\"passengers\":2}"
        Log.d("TransferRequest", res)
        return res
    }
}
