package com.example.flybooking.model

import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.FlightOffer
import com.example.flybooking.ui.viewmodel.HotelObject
import com.example.flybooking.ui.viewmodel.TransferObject

data class Booking(
    val id: String,
    var activities: List<Activity>,
    var hotels: List<HotelObject>,
    var flights: List<FlightOffer>,
    var transfers: List<TransferObject>
) {
    constructor() : this("0", emptyList(), emptyList(), emptyList(), emptyList())
}