package com.example.flybooking.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchInputData(
    val departure: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String,
    val adults: Int,
    val children: Int,
    val budget: Double
)