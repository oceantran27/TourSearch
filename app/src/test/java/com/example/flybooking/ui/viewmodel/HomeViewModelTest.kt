package com.example.flybooking.ui.viewmodel

import com.example.flybooking.model.City
import com.example.flybooking.repository.Repository
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        repository = mockk()

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun testIncreaseAdultCountUpdatesPassengerCountCorrectly() = runTest {
        val initialState = viewModel.homeUiState.first()

        viewModel.increaseAdultCount()

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
        val departure = City("", "", "US") // Empty name
        val destination = City("London", "LON", "UK")
        val moneyAmount = 1000.0

        val isValid = viewModel.checkValidForm(departure, destination, moneyAmount)

        assertEquals(false, isValid)
    }
}
