package com.example.weatherapp.data.model

class WeatherResponse {
    var coord:Coordinate?=null
    var weather:List<Weather>?=null
    var main:Main?=null
    var wind:Wind?=null
    var sys:Sys?=null
    var name:String=""
    var dt:Long?=null

    class Coordinate{
        var lon:String=""
        var lat:String=""
    }
    class Weather{
        var id:String=""
        var main:String=""
        var description:String=""
        var icon:String=""
    }

    class Main{
        var temp:Double?=null
        var feels_like=""
        var temp_min:Double?=null
        var temp_max:Double?=null
        var pressure:Double?=null
        var humidity:Double?=null
        var sea_level=""
        var grnd_level=""
    }
    class Wind{
        var speed=""
        var deg=""
        var gust=""
    }
    class Sys{
        var country=""
        var sunrise:Long?=null
        var sunset:Long?=null
    }
}