package com.example.flybooking.ui.screens.home.activities

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.flybooking.activity.ActivityDetailActivity
import com.example.flybooking.model.response.Activity
import com.example.flybooking.model.response.ActivityCard
import com.example.flybooking.ui.screens.others.SelectableCard
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import kotlinx.serialization.json.Json

@Composable
fun ActivitiesEditScreen(
    modifier: Modifier = Modifier,
    activities: List<ActivityCard>,
    activitiesViewModel: ActivitiesViewModel
) {
    val context = LocalContext.current
    LazyColumn (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(activities) { card ->
//            PreviewActivityCard(
//                activity = card.activity,
//                selected = card.selected,
//                onClick = {
//                    if (card.selected) {
//                        activitiesViewModel.deselectActivity(card)
//                    } else {
//                        activitiesViewModel.selectActivity(card)
//                    }
//                }
//            )
            SelectableCard(
                onClick = {
                    if (card.selected) {
                        activitiesViewModel.deselectActivity(card)
                    } else {
                        activitiesViewModel.selectActivity(card)
                    }
                },
                onLongClick = {
                    val jsonActivity = Json.encodeToString(Activity.serializer(), card.activity)
                    val intent = Intent(context, ActivityDetailActivity::class.java).apply {
                        putExtra("activity_data", jsonActivity)
                    }
                    context.startActivity(intent)
                },
                selected = card.selected
            ) {
                PreviewActivityCard2(
                    activity = card.activity
                )
            }
        }
    }
}