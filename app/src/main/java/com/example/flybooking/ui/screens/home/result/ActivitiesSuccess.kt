package com.example.flybooking.ui.screens.home.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flybooking.model.response.ActivityCard
import com.example.flybooking.ui.screens.home.activities.PreviewActivityCard

@Composable
fun ActivitiesSuccess(
    activities: List<ActivityCard>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SectionHeader(
            title = "Activities",
            onEdit = {},
            modifier = Modifier.fillMaxWidth()
        )
        RecommendActivities(activities)
    }
}

@Composable
fun RecommendActivities(
    activities: List<ActivityCard>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (card in activities) {
            PreviewActivityCard(
                activity = card.activity,
                selected = false,
                onClick = {}
            )
        }
    }
}