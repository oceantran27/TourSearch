package com.example.flybooking.ui.screens.home

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateInput(
    date: String,
    onDateChange: () -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.calendar_ic),
            contentDescription = date,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Text(
                text = date,
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = onDateChange)
            )
        }
    }
}

@Preview
@Composable
fun DateInputPreview() {
    DateInput(date = "12 Jan, 2022", onDateChange = {})
}

@Composable
fun DateInputs(
    departureDate: Long,
    returnDate: Long,
    onDepartureDateChange: (Long) -> Unit,
    onReturnDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Định dạng ngày
    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    val departureDateString = dateFormat.format(Date(departureDate))
    val returnDateString = dateFormat.format(Date(returnDate))

    // Date Picker Dialogs
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Date Picker cho ngày đi
    val departureDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDepartureDateChange(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Date Picker cho ngày về
    val returnDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onReturnDateChange(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        LabeledField(label = "Departure date", modifier = Modifier.weight(1f)) {
            DateInput(
                date = departureDateString,
                onDateChange = { departureDatePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        LabeledField(label = "Return date", modifier = Modifier.weight(1f)) {
            DateInput(
                date = returnDateString,
                onDateChange = { returnDatePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}