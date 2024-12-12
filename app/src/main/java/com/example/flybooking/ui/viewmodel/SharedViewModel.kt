package com.example.flybooking.ui.viewmodel

import com.example.flybooking.model.Booking
import com.example.flybooking.model.City

object SharedViewModel {
    var departure: City? = null
    var destination: City? = null
    var departureDate: String? = null
    var returnDate: String? = null
    var adults: Int = 1
    var children: Int = 0
    var budget: Int = 0
    var activitiesViewModel: ActivitiesViewModel? = null
    var hotelViewModel: HotelViewModel? = null
    var transferViewModel: TransferViewModel? = null
    var flightViewModel: FlightViewModel? = null
    var booking: Booking? = null
    fun set(
        departure: City,
        destination: City,
        departureDate: String,
        returnDate: String,
        adults: Int,
        children: Int,
        budget: Int
    ) {
        this.departure = departure
        this.destination = destination
        this.departureDate = departureDate
        this.returnDate = returnDate
        this.adults = adults
        this.children = children
        this.budget = budget
    }
}
