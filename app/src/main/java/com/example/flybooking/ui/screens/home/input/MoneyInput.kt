package com.example.flybooking.ui.screens.home.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flybooking.R

@Composable
fun MoneyInput(
    onMoneyUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isValidOption by remember { mutableStateOf(true) }
    val localFocusManager = LocalFocusManager.current

    fun validateMoneyInput(input: String): Boolean {
        val regex = Regex("^[0-9]+(\\.[0-9]{1,2})?$")
        return (input.isEmpty()) || (regex.matches(input) && input.toDoubleOrNull() != null && input.toDouble() > 0)
    }

    LabeledField(label = "Enter amount of money ($)") {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                isValidOption = validateMoneyInput(it)
                if (isValidOption) {
                    onMoneyUpdate(it)
                }
            },
            label = {
                Text(
                    text = if (isValidOption) "" else "Please enter a valid money amount",
                    color = if (isValidOption) Color.Gray else Color.Red
                )
            },
            isError = !isValidOption,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.cash),
                    contentDescription = null
                )
            },
            keyboardActions = KeyboardActions(onDone = {
                localFocusManager.clearFocus()
            }),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            )
        )
    }
}
