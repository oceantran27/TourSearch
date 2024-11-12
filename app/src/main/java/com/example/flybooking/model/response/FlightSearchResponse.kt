package com.example.flybooking.model.response

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
//    val travelerPricings: List<TravelerPricing>
)

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

