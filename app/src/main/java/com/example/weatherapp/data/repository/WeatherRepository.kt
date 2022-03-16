package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.ApiService
import com.example.weatherapp.data.network.SafeApiRequest
import javax.inject.Inject

/*-----------NEWS REPOSITORY DATASOURCE------------*/
class WeatherRepository @Inject constructor(private val apiService: ApiService): SafeApiRequest() {
    suspend fun getLatestWeather(stateName:String)=
        apiRequest {
            var url:String="data/2.5/weather?q="+stateName+"&appid="+ BuildConfig.API_KEY
            apiService.GetLatestWeather(url)
        }
    suspend fun getStateName(lat:Double,lon:Double)=
        apiRequest {
            apiService.ProvideState(lat,lon,5,BuildConfig.API_KEY)
        }


    }
