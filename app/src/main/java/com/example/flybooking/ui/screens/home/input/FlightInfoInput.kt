package com.example.flybooking.ui.screens.home.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.flybooking.R
import com.example.flybooking.model.City

@Composable
fun DepartureAndDestinationInput(
    onDepartureSelected: (City) -> Unit,
    onDestinationSelected: (City) -> Unit,
    onDepartureInvalidOption: () -> Unit,
    onDestinationInvalidOption: () -> Unit,
//    prePickedDeparture: City,
//    prePickedDestination: City,
    modifier: Modifier = Modifier
) {
    val departureCities = listOf(
        City("Hanoi", "HAN", "VN"),
    )
    val destinationCities = listOf(
        City("Paris", "PAR", "FR"),
    )
    // Dropdown có tìm kiếm cho các thành phố Departure
    LabeledField(label = "From") {
        SearchableDropdownMenuField(
            label = "",
            options = departureCities,
            onOptionSelected = { onDepartureSelected(it) },
            painter = painterResource(id = R.drawable.from_ic),
            onInvalidOption = onDepartureInvalidOption,
            //prePickedOption = prePickedDeparture,
            modifier = Modifier.fillMaxWidth().semantics {
                contentDescription = "FROM_LABEL"
            }
        )
    }

    // Dropdown có tìm kiếm cho các thành phố Destination
    LabeledField(label = "To") {
        SearchableDropdownMenuField(
            label = "",
            options = destinationCities,
            onOptionSelected = { onDestinationSelected(it) },
            painter = painterResource(id = R.drawable.to_ic),
            onInvalidOption = onDestinationInvalidOption,
            //prePickedOption = prePickedDestination,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TicketClassInput(
    onClassSelected: (String) -> Unit,
    selectedOption: String,
    modifier: Modifier = Modifier
) {
    val classes = listOf("Economy", "Business Class", "First Class")
    // Dropdown không tìm kiếm cho các hạng vé
    LabeledField("Ticket class") {
        NonSearchableDropdownMenuField(
            label = "Class",
            selectedOption = selectedOption,
            options = classes,
            onOptionSelected = { onClassSelected(it) },
            painter = painterResource(id = R.drawable.seat_black_ic),
            modifier = Modifier.fillMaxWidth()
        )
    }
}