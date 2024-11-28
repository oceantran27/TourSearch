package com.example.flybooking.model

import com.example.flybooking.model.response.amadeus.GeoCode

data class TransferLocInfo(
    val addressLine: String = "addressLine",
    val geoCode: GeoCode,
)

data class TransferInfo(
    val from: TransferLocInfo,
    val to: TransferLocInfo,
    val date: String
)