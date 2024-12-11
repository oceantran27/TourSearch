package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class FlightSearchResponse(
    val data: List<FlightOffer> = emptyList()
)

@Serializable
data class FlightOffer(
    val id: String = "0",
    val itineraries: List<Itinerary> = emptyList(),
    val price: Price = Price(),
    val validatingAirlineCodes: List<String>? = emptyList()
) {
    constructor() : this("0", emptyList(), Price(), emptyList())

    companion object {
        fun empty() = FlightOffer(
            id = "0",
            itineraries = emptyList(),
            price = Price(),
            validatingAirlineCodes = emptyList()
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
    val duration: String = "",
    val segments: List<Segment> = emptyList()
)

@Serializable
data class Segment(
    val departure: AirportInfo = AirportInfo(),
    val arrival: AirportInfo = AirportInfo(),
    val carrierCode: String = "",
    val duration: String = ""
)

@Serializable
data class AirportInfo(
    val iataCode: String = "",
    val at: String = ""
)

@Serializable
data class Price(
    val currency: String = "USD",
    val total: String = "0.0",
    val base: String = "0.0"
)

@Serializable
data class TravelerPricing(
    val travelerType: String = "",
    val activityPrice: ActivityPrice = ActivityPrice()
)

