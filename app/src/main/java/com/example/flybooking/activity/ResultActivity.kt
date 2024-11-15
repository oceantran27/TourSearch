package com.example.flybooking.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.model.response.ActivityCard
import com.example.flybooking.ui.screens.home.result.ResultScene
import com.example.flybooking.ui.theme.FlyBookingTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed interface ReceivedData {
    @Serializable
    data class Activities(val activities: List<ActivityCard>) : ReceivedData
}

class ResultActivity : ComponentActivity() {
    private lateinit var searchInputData: SearchInputData
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val receivedActivities = mutableStateOf<ReceivedData.Activities?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra("search_input_data")!!
        searchInputData = Json.decodeFromString(SearchInputData.serializer(), jsonString)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val jsonData = result.data?.getStringExtra("result_data")!!
                val resultData = jsonData.let {
                    Json.decodeFromString(ReceivedData.serializer(), it)
                }
                if (resultData is ReceivedData.Activities) {
                    receivedActivities.value = resultData
                }
            }

        }

        val intent = Intent(this, FlightSearchActivity::class.java).apply {
            putExtra("search_input_data", jsonString)
        }

        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    ResultScene(
                        searchInputData = searchInputData,
//                        intent = intent,
//                        limitCost = searchInputData.budget,
                        modifier = Modifier.padding(paddingValues),
                        receivedActivities = receivedActivities.value?.activities?.map {
                            it.activity
                        } ?: emptyList(),
                        activityResultLauncher = activityResultLauncher
                    )
                }
            }
        }
    }
}
