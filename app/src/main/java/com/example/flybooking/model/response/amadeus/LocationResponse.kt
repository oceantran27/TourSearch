package com.example.flybooking.model.response.amadeus

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue

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
): Parcelable {
    constructor(): this(0.0, 0.0)
    override fun toString(): String {
        return "$latitude,$longitude"
    }

    override fun equals(other: Any?): Boolean {
        if (other is GeoCode) {
            return (latitude - other.latitude).absoluteValue < 0.0001 &&
                    (longitude - other.longitude).absoluteValue < 0.0001
        }
        return false
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}