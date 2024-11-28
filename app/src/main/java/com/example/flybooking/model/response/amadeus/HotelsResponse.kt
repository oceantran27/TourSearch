package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class HotelsResponse(
    val data: List<Hotel>
)

@Serializable
data class Hotel(
    val name: String,
    val geoCode: GeoCode
)