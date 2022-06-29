package com.example.petder.DateTime

import java.text.SimpleDateFormat
import java.util.*

class DateTime {

    fun formatDateTimeFromString(format: String, dateTimeString: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTimeString)
        val formatter = SimpleDateFormat(format)
        val output = formatter.format(parser)
        return output
    }

    fun convertUTCToGMT(dateTime: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val parser = formatter.parse(dateTime)
        val time = SimpleDateFormat("HH:mm").format(parser)
        return time.toString()
    }
}