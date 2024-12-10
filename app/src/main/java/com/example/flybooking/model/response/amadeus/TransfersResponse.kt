package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class TransfersResponse(
    val data: List<Transfer>
)

@Serializable
data class Transfer(
    val id: String,
    val transferType: String,
    val vehicle: Vehicle,
    val serviceProvider: ServiceProvider,
    val converted: Converted,
    val methodsOfPaymentAccepted: List<String>? = null,
) {
    constructor(): this("0", "transferType", Vehicle("description", null, emptyList()), ServiceProvider("code", "name", "logoUrl"), Converted("0.0", "currencyCode"), emptyList())
}

@Serializable
data class Vehicle(
    val description: String,
    val imageURL: String? = null,
    val seats: List<Seat>,
)

@Serializable
data class Seat(
    val count: Int
)

@Serializable
data class ServiceProvider(
    val code: String,
    val name: String,
    val logoUrl: String
)

@Serializable
data class Converted(
    val monetaryAmount: String,
    val currencyCode: String
)