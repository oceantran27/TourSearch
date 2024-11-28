package com.example.flybooking.model

import com.example.flybooking.model.response.amadeus.ActivityPrice
import kotlinx.serialization.Serializable

@Serializable
data class Flight(
    val departureIataCode: String,
    val arrivalIataCode: String,
    val departureDate: String,
    val arrivalDate: String,
    val duration: String,
    val activityPrice: ActivityPrice
)

