package com.example.flybooking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.ui.screens.activities.ActivitiesSearchScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import kotlinx.serialization.json.Json

class ActivitiesSearchActivity : ComponentActivity() {
    private lateinit var searchInputData: SearchInputData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra("search_input_data")!!
        if (jsonString != null) {
            searchInputData = Json.decodeFromString(SearchInputData.serializer(), jsonString)
        }

        val intent = Intent(this, FlightSearchActivity::class.java).apply {
            putExtra("search_input_data", jsonString)
        }

        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    ActivitiesSearchScreen(
                        searchInputData = searchInputData,
                        intent = intent,
                        limitCost = searchInputData.budget,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}
