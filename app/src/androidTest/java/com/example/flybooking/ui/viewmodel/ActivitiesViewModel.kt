package com.example.flybooking.ui.viewmodel

import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.ActivityCard
import com.example.flybooking.model.response.amadeus.ActivityPrice
import com.example.flybooking.model.response.amadeus.GeoCode
import com.example.flybooking.repository.Repository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ActivitiesViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: ActivitiesViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = ActivitiesViewModel(repository)
    }

    @Test
    fun testSelectActivityUpdatesStateAndCost() = runTest {
        // Mock initial state
        val geoCode = GeoCode(latitude = 48.8566, longitude = 2.3522)
        val activity = Activity(
            name = "Eiffel Tower Tour",
            description = "Guided tour of the Eiffel Tower.",
            geoCode = geoCode,
            price = ActivityPrice(amount = "50", currencyCode = "EUR"),
            pictures = listOf("url_to_picture")
        )
        val card = ActivityCard(activity, false)
        viewModel.setTestUiState(
            ActivitiesUiState.Success(
                cards = listOf(card),
                selected = emptyList(),
                totalCost = 0.0
            )
        )

        // Invoke the method
        viewModel.selectActivity(card)

        // Assert UI state
        val uiState = viewModel.activitiesUiState
        assertTrue(uiState is ActivitiesUiState.Success)
        val successState = uiState as ActivitiesUiState.Success
        assertTrue(successState.cards[0].selected)
        assertTrue(successState.selected.contains(activity))
        assertTrue(successState.totalCost > 0) // Ensure cost is updated
    }

}
