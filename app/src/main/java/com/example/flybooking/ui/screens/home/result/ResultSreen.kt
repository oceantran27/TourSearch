package com.example.flybooking.ui.screens.home.result

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.ActivitiesEditActivity
import com.example.flybooking.activity.ActivityDetailActivity
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.model.response.Activity
import com.example.flybooking.model.response.ActivityCard
import com.example.flybooking.model.response.FlightOffer
import com.example.flybooking.ui.screens.home.activities.PreviewActivityCard
import com.example.flybooking.ui.screens.others.LoadingAnimation
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import com.example.flybooking.ui.viewmodel.FlightUiState
import com.example.flybooking.ui.viewmodel.FlightViewModel
import com.example.flybooking.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun ResultScene(
    modifier: Modifier = Modifier,
    activitiesViewModel: ActivitiesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    flightViewModel: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory),
    searchInputData: SearchInputData,
//    intent: Intent? = null,
//    limitCost: Double,
    receivedActivities: List<Activity> = emptyList(),
    activityResultLauncher: ActivityResultLauncher<Intent>
) {
    val coroutineScope = rememberCoroutineScope()
    val activitiesUiState = activitiesViewModel.activitiesUiState
    val flightsUiState = flightViewModel.flightUiState
    val context = LocalContext.current

    val isLoading = (activitiesUiState is ActivitiesUiState.Loading
            || flightsUiState is FlightUiState.Loading)

    val isError = (activitiesUiState is ActivitiesUiState.Error
            || flightsUiState is FlightUiState.Error)

    var activities: List<Activity> = if (receivedActivities.isNotEmpty()) {
        activitiesViewModel.initWithSelected(receivedActivities)
        receivedActivities
    } else if (activitiesUiState is ActivitiesUiState.Success) {
        activitiesUiState.selected
    } else {
        emptyList()
    }
    var offers: List<FlightOffer> = emptyList()

    if (isError) {
        Text(text = "Error")
    } else if (isLoading) {
        LoadingScene(modifier = modifier.fillMaxSize())
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp)
        ) {
            // -------------------------- Activities -------------------------- //
            item {
                SectionHeader(
                    title = "Activities",
                    onEdit = {
                        SharedViewModel.activitiesViewModel = activitiesViewModel
                        val intent = Intent(context, ActivitiesEditActivity::class.java)
                        activityResultLauncher.launch(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(activities) { activity ->
                val card = ActivityCard(activity)
                PreviewActivityCard(
                    activity = card.activity,
                    onClick = {
                        val jsonActivity = Json.encodeToString(Activity.serializer(), activity)
                        val intent = Intent(context, ActivityDetailActivity::class.java).apply {
                            putExtra("activity_data", jsonActivity)
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // -------------------------- Flights -------------------------- //
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                FlightSuccess(
                    offers = offers,
                    onEdit = {
//                        val intent = Intent(context, ActivitiesEditActivity::class.java).apply {
//                            val jsonData = Json.encodeToString(
//                                ListSerializer(ActivityCard.serializer()),
//                                (activitiesUiState as ActivitiesUiState.Success).cards
//                            )
//                            putExtra("activity_cards", jsonData)
//                        }
//                        activityResultLauncher.launch(intent)
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            activitiesViewModel.searchActivities(
                destination = searchInputData.destination
            )
        }
        coroutineScope.launch {
            flightViewModel.searchFlights(
                departure = searchInputData.departure,
                destination = searchInputData.destination,
                departureDate = searchInputData.departureDate,
                returnDate = searchInputData.returnDate,
                adults = searchInputData.adults,
                children = searchInputData.children,
            )
        }
    }
}

@Composable
fun LoadingScene(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(
            circleSize = 12.dp,
            spaceBetween = 20.dp,
            travelDistance = 15.dp,
        )
    }
}


@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        IconButton(
            onClick = onEdit
        )  {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }
    }
}