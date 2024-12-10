package com.example.flybooking.model

import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.FlightOffer
import com.example.flybooking.ui.viewmodel.HotelObject
import com.example.flybooking.ui.viewmodel.TransferObject

data class Booking(
    val id: String,
    val activities: List<Activity>,
    val hotel: HotelObject,
    val flight: FlightOffer,
    val transfers: List<TransferObject>
) {
    constructor(): this("0", emptyList(), HotelObject(), FlightOffer(), emptyList())
}