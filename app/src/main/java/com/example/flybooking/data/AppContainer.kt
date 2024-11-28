package com.example.flybooking.data

import android.content.Context
import com.example.flybooking.network.AmadeusApiService
import com.example.flybooking.network.TripAdvisorApiService
import com.example.flybooking.repository.Repository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val repository: Repository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val AMADEUS_BASE = "https://test.api.amadeus.com/"
    private val TRIP_ADVISOR_BASE = "https://api.content.tripadvisor.com/api/v1/location/"
    val json = Json { ignoreUnknownKeys = true }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofitAmadeus: Retrofit = Retrofit.Builder()
        .baseUrl(AMADEUS_BASE)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val retrofitTripAdvisor: Retrofit = Retrofit.Builder()
        .baseUrl(TRIP_ADVISOR_BASE)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val amadeusApiService: AmadeusApiService = retrofitAmadeus.create(AmadeusApiService::class.java)
    private val tripAdvisorApiService: TripAdvisorApiService = retrofitTripAdvisor.create(TripAdvisorApiService::class.java)

    override val repository: Repository by lazy {
        Repository(amadeusApiService, tripAdvisorApiService)
    }
}
