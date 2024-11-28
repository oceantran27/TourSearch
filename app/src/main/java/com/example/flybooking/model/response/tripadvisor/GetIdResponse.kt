package com.example.flybooking.model.response.tripadvisor

import kotlinx.serialization.Serializable

@Serializable
data class GetIdResponse(
    val data: List<Location>
)

@Serializable
data class Location(
    val location_id: String
)