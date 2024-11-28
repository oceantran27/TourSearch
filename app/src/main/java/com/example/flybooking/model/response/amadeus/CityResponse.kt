package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    val data: List<CityData>
)

@Serializable
data class CityData(
    val geoCode: GeoCode
)
