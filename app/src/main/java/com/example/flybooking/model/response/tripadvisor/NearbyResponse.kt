package com.example.flybooking.model.response.tripadvisor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NearbyResponse(
    val data: List<Nearby>
)

@Serializable
data class Nearby(
    @SerialName("location_id") val locationId: String,
)