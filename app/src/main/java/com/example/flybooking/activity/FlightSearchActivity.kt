package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.ui.screens.home.flights.FlightSearchScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import kotlinx.serialization.json.Json

class FlightSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonInputData = intent.getStringExtra("search_input_data")!!
        val jsonActivities = intent.getStringExtra("selected_activities")!!
        val inputData = Json.decodeFromString(SearchInputData.serializer(), jsonInputData)
        setContent {
            FlyBookingTheme {
                FlightSearchScreen(
                    inputData = inputData,
                )
            }
        }
    }
}