package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class TransfersResponse(
    val data: List<Transfer> = emptyList()
)

@Serializable
data class Transfer(
    val id: String = "0",
    val transferType: String = "transferType",
    val vehicle: Vehicle = Vehicle(),
    val serviceProvider: ServiceProvider = ServiceProvider(),
    val converted: Converted = Converted(),
    val methodsOfPaymentAccepted: List<String>? = emptyList()
) {
    constructor() : this(
        id = "0",
        transferType = "transferType",
        vehicle = Vehicle(),
        serviceProvider = ServiceProvider(),
        converted = Converted(),
        methodsOfPaymentAccepted = emptyList()
    )
}

@Serializable
data class Vehicle(
    val description: String = "description",
    val imageURL: String? = null,
    val seats: List<Seat> = emptyList()
) {
    constructor() : this(
        description = "description",
        imageURL = null,
        seats = emptyList()
    )
}

@Serializable
data class Seat(
    val count: Int = 0
) {
    constructor() : this(count = 0)
}

@Serializable
data class ServiceProvider(
    val code: String = "code",
    val name: String = "name",
    val logoUrl: String = "logoUrl"
) {
    constructor() : this(
        code = "code",
        name = "name",
        logoUrl = "logoUrl"
    )
}

@Serializable
data class Converted(
    val monetaryAmount: String = "0.0",
    val currencyCode: String = "USD"
) {
    constructor() : this(
        monetaryAmount = "0.0",
        currencyCode = "USD"
    )
}
