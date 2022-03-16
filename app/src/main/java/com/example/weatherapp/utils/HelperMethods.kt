package com.example.weatherapp.utils

import android.text.format.DateFormat
import java.util.*

class HelperMethods {
    companion object{
        var temp:String=""
        fun convertKelvinToCelcius(data:Double):String{
            return (data -273).toInt().toString()
        }
        fun convertToPaskal(data:Double):String{
            return (data/1000).toInt().toString()
        }

        fun getDate(time: Long): String? {
            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.setTimeInMillis(time * 1000)
            return DateFormat.format("hh:mm a", cal).toString()
        }
    }
}