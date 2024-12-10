package com.example.flybooking.ui.viewmodel

import com.example.flybooking.model.City
import com.example.flybooking.repository.Repository
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        // Mock repository
        repository = mockk()

        // Initialize ViewModel
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun testIncreaseAdultCountUpdatesPassengerCountCorrectly() = runTest {
        // Default state
        val initialState = viewModel.homeUiState.first()

        // Action
        viewModel.increaseAdultCount()

        // Assert
        val updatedState = viewModel.homeUiState.first()
        assertEquals(initialState.passengerAdultCount + 1, updatedState.passengerAdultCount)
    }

    @Test
    fun testCheckValidFormReturnsTrueWhenDataIsValid() {
        // Sample valid cities and money amount
        val departure = City("New York", "NYC", "US")
        val destination = City("London", "LON", "UK")
        val moneyAmount = 1000.0

        // Action
        val isValid = viewModel.checkValidForm(departure, destination, moneyAmount)

        // Assert
        assertEquals(true, isValid)
    }

    @Test
    fun testCheckValidFormReturnsFalseWhenDataIsInvalid() {
        // Sample invalid cities and money amount
        val departure = City("", "", "US") // Empty name
        val destination = City("London", "LON", "UK")
        val moneyAmount = 1000.0

        // Action
        val isValid = viewModel.checkValidForm(departure, destination, moneyAmount)

        // Assert
        assertEquals(false, isValid)
    }
}
