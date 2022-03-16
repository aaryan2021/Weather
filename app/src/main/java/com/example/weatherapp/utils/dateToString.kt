package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

private fun Date.dateToString(format: String): String {
    //simple date formatter
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        
        //return the formatted date string
        return dateFormatter.format(this)
    }
    