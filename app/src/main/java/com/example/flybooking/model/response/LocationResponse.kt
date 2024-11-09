package com.example.flybooking.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val data: List<LocationData>
)

@Serializable
data class LocationData(
    val name: String,
    val geoCode: GeoCode
)

@Parcelize
@Serializable
data class GeoCode(
    val latitude: Double,
    val longitude: Double
): Parcelable