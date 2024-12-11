package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class FlightSearchResponse(
    val data: List<FlightOffer>
)

@Serializable
data class FlightOffer(
    val id: String,
    val itineraries: List<Itinerary>,
    val price: Price,
    val validatingAirlineCodes: List<String>? = null
//    val travelerPricings: List<TravelerPricing>
) {
    constructor(): this("0", emptyList(), Price("USD", "0.0", "0.0"), emptyList())
    companion object {
        fun empty() = FlightOffer(
            id = "0",
            itineraries = emptyList(),
            price = Price(
                currency = "USD",
                total = "0.0",
                base = "0.0")
        )
        fun mock() = FlightOffer(
            id = "1",
            itineraries = listOf(
                Itinerary(
                    duration = "PT15H30M",
                    segments = listOf(
                        Segment(
                            departure = AirportInfo(
                                iataCode = "HAN",
                                at = "2024-12-15T09:00:00"
                            ),
                            arrival = AirportInfo(
                                iataCode = "CDG",
                                at = "2024-12-15T20:30:00"
                            ),
                            carrierCode = "VN",
                            duration = "PT11H30M"
                        )
                    )
                )
            ),
            price = Price(
                currency = "USD",
                total = "1200.00",
                base = "1000.00"
            ),
            validatingAirlineCodes = listOf("VN")
        )
    }
}

@Serializable
data class Itinerary(
    val duration: String,
    val segments: List<Segment>
)

@Serializable
data class Segment(
    val departure: AirportInfo,
    val arrival: AirportInfo,
    val carrierCode: String,
    val duration: String
)

@Serializable
data class AirportInfo(
    val iataCode: String,
    val at: String
)

@Serializable
data class Price(
    val currency: String,
    val total: String,
    val base: String
)

@Serializable
data class TravelerPricing(
    val travelerType: String,
    val activityPrice: ActivityPrice
)

