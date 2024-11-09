package com.example.flybooking.data

import android.content.Context
import com.example.flybooking.network.ApiService
import com.example.flybooking.repository.Repository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val repository: Repository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "https://test.api.amadeus.com/"
    val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    override val repository: Repository by lazy {
        Repository(apiService)
    }
}
