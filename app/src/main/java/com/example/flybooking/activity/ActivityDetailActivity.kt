package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flybooking.model.response.Activity
import com.example.flybooking.ui.screens.home.activities.ActivityDetailScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import kotlinx.serialization.json.Json

class ActivityDetailActivity : ComponentActivity() {
    private lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonActivity = intent.getStringExtra("activity_data")!!
        activity = Json.decodeFromString(Activity.serializer(), jsonActivity)

        setContent {
            FlyBookingTheme {
                ActivityDetailScreen(activity)
            }
        }
    }
}
