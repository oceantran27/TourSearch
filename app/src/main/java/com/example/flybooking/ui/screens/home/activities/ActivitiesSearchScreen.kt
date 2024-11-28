package com.example.flybooking.ui.screens.home.activities

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.ActivityCard
import com.example.flybooking.ui.screens.others.LoadingAnimation
import com.example.flybooking.ui.theme.ButtonBackground
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun ActivitiesSearchScreen(
    activitiesViewModel: ActivitiesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    searchInputData: SearchInputData,
    intent: Intent? = null,
    limitCost: Double,
    modifier: Modifier = Modifier
) {
    val activitiesState = activitiesViewModel.activitiesUiState
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tour Activities in ${searchInputData.destination}", style = MaterialTheme.typography.titleLarge)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when (activitiesState) {
                is ActivitiesUiState.Loading -> {
                    LoadingScene(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ActivitiesUiState.Error -> {
                    Text(text = "Error loading activities")
                }
                is ActivitiesUiState.Success -> {
                    SuccessScene(
                        successState = activitiesState,
                        deselectActivity = activitiesViewModel::deselectActivity,
                        selectActivity = activitiesViewModel::selectActivity,
                        totalLabelModifier = Modifier.align(Alignment.TopEnd),
                        limitCost = limitCost
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize().background(Color.Transparent),
            ) {
                Button(
                    onClick = {
                        val json = Json { encodeDefaults = true }
                        val selectedActivities = json.encodeToString(
                            ListSerializer(Activity.serializer()),
                            (activitiesState as ActivitiesUiState.Success).selected
                        )
                        intent?.putExtra("selected_activities", selectedActivities)
                        context.startActivity(intent)
                    },
                    enabled = (activitiesState is ActivitiesUiState.Success),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBackground)
                ) {
                    Text(text = "Proceed to Flight Booking")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            activitiesViewModel.searchActivities(
                destination = searchInputData.destination
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
            travelDistance = 15.dp
        )
    }
}

@Composable
fun SuccessScene(
    successState: ActivitiesUiState.Success,
    deselectActivity: (ActivityCard) -> Unit,
    selectActivity: (ActivityCard) -> Unit,
    modifier: Modifier = Modifier,
    totalLabelModifier: Modifier,
    limitCost: Double
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(successState.cards) { card ->
            PreviewActivityCard(
                activity = card.activity,
                selected = card.selected,
                onClick = {
                    if (card.selected) {
                        deselectActivity(card)
                    } else {
                        selectActivity(card)
                    }
                }
            )
        }
    }
    if (successState.totalCost > 0.0) {
        val color = if (successState.totalCost <= limitCost) {
            Color.Green
        } else {
            Color.Red
        }
        Box(
            modifier = totalLabelModifier.padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 25))
                    .background(color.copy(alpha = 0.9f))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Total: $${"%.2f".format(successState.totalCost)}",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

