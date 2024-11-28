package com.example.flybooking.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HotelsResponse(
    val data: List<Hotel>
)

@Serializable
data class Hotel(
    val name: String,
    val hotelId: String,
    val geoCode: GeoCode,
    val address: Address,
    val distance: Distance,
)

@Serializable
data class Distance(
    val value: Float,
    val unit: String,
)

@Serializable
data class Address(
    val countryCode: String,
)

