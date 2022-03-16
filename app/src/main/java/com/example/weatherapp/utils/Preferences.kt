package com.example.weatherapp.utils

import android.content.Context
import android.preference.PreferenceManager

class Preferences(var context: Context) {

    companion object{
        const val Temprature:String="Temprature"
        const val Pressure:String="Pressure"
        const val Location:String="Location"
        const val Time:String="Time"
        const val Humidity:String="Humidity"
        const val Status:String="Status"


    }


    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    var temp = preferences.getString(Temprature, "")
        set(value) = preferences.edit().putString(Temprature, value).apply()
    var pressure = preferences.getString(Pressure, "")
        set(value) = preferences.edit().putString(Pressure, value).apply()
    var location = preferences.getString(Location, "pune")
        set(value) = preferences.edit().putString(Location, value).apply()
    var time = preferences.getString(Time, "")
        set(value) = preferences.edit().putString(Time, value).apply()
    var humidity = preferences.getString(Humidity, "")
        set(value) = preferences.edit().putString(Humidity, value).apply()
    var status = preferences.getString(Status, "")
        set(value) = preferences.edit().putString(Status, value).apply()


}
