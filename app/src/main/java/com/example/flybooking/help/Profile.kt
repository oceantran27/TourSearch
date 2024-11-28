package com.example.flybooking.help

fun convertTimestampToDate(timestamp: Long): String {
    return if (timestamp != 0L) {
        val date = java.util.Date(timestamp)
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
        sdf.format(date)
    } else {
        ""
    }
}