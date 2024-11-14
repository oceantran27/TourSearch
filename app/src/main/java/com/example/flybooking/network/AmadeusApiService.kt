package com.example.flybooking.network

import com.example.flybooking.model.response.AccessTokenResponse
import com.example.flybooking.model.response.ActivitiesResponse
import com.example.flybooking.model.response.FlightSearchResponse
import com.example.flybooking.model.response.LocationResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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
}
