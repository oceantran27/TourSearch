package com.example.flybooking.network

import com.example.flybooking.BuildConfig
import com.example.flybooking.model.response.tripadvisor.DetailsResponse
import com.example.flybooking.model.response.tripadvisor.NearbyResponse
import com.example.flybooking.model.response.tripadvisor.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TripAdvisorApiService {

    // Lấy ID từ tọa độ
    @GET("nearby_search")
    suspend fun getNearbyIds(
        @Query("key") key: String = BuildConfig.TRIP_API_KEY,
        @Query("category") category: String = "hotels",
        @Query("latLong") latLong: String,
        @Query("radius") radius: Int = 10,
        @Query("radiusUnit") unit: String = "km"
    ): Response<NearbyResponse>

    // Lấy details từ ID
    @GET("{locationId}/details")
    suspend fun getDetails(
        @Path("locationId") locationId: String,
        @Query("key") key: String = BuildConfig.TRIP_API_KEY
    ): Response<DetailsResponse>

    // Lấy ảnh từ ID
    @GET("{locationId}/photos")
    suspend fun getPhotos(
        @Path("locationId") locationId: String,
        @Query("key") key: String = BuildConfig.TRIP_API_KEY
    ): Response<PhotosResponse>
}