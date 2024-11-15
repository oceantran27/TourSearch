package com.example.flybooking.ui.screens.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropdownMenuField(
    label: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    onInvalidOption: () -> Unit,
    painter: Painter,
    prePickedOption: String = "",
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(prePickedOption) }
    var isValidOption by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                expanded = true
            },
            label = {
                Text(
                    text = if (isValidOption) label else "Please select a valid flight option",
                    color = if (isValidOption) Color.Gray else Color.Red
                )
            },
            isError = !isValidOption,
            leadingIcon = {
                Icon(
                    painter = painter,
                    contentDescription = "From_icon",
                    tint = Color.Unspecified
                )
            },
            singleLine = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
            ,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
                isValidOption = options.contains(searchText)
                if (!isValidOption) {
                    onInvalidOption()
                }
            }
        ) {
            val filteredOptions = options.filter { it.contains(searchText, ignoreCase = true) }

            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        searchText = option
                        onOptionSelected(option)
                        expanded = false
                        focusManager.clearFocus()
                        isValidOption = true
                    },
                    modifier = if (option == "NewYork") {
                        Modifier.semantics {
                            contentDescription = "CITY_TAG"
                        }
                    } else {
                        Modifier
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonSearchableDropdownMenuField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    painter: Painter,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                Icon(
                    painter = painter,
                    contentDescription = "Class_icon",
                    tint = Color.Unspecified
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
