package com.example.flybooking


import android.app.Application
import com.example.flybooking.data.AppContainer
import com.example.flybooking.data.DefaultAppContainer

class FlightBookingApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
