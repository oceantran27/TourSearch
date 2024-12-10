package com.example.flybooking.model

import com.example.flybooking.model.response.amadeus.GeoCode
import kotlinx.serialization.Serializable

@Serializable
data class TransferLocInfo(
    val addressLine: String = "addressLine",
    val geoCode: GeoCode,
)

@Serializable
data class TransferInfo(
    val from: TransferLocInfo,
    val to: TransferLocInfo,
    val date: String
)