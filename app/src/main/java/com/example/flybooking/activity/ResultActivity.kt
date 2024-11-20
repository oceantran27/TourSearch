package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.ui.screens.home.result.ResultScene
import com.example.flybooking.ui.theme.FlyBookingTheme
import kotlinx.serialization.json.Json

class ResultActivity : ComponentActivity() {
    private lateinit var searchInputData: SearchInputData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra("search_input_data")!!
        searchInputData = Json.decodeFromString(SearchInputData.serializer(), jsonString)

        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    ResultScene(
                        searchInputData = searchInputData,
//                        intent = intent,
//                        limitCost = searchInputData.budget,
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}
