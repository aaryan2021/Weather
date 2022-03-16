package com.example.weatherapp.data.network

import com.example.weatherapp.data.model.StateResponse
import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/*-----------SERVICE INTERFACE FOR RETROFIT NETWROK CALL------------*/
interface ApiService {
    @GET
    suspend fun GetLatestWeather(@Url url:String): Response<WeatherResponse>

    @GET
    fun ProvideWeather(@Url url:String): Call<WeatherResponse>

    @GET("geo/1.0/reverse")
    suspend fun ProvideState(@Query("lat") lat:Double,@Query("lon") lon:Double,@Query("limit")limit:Int,@Query("appid")appid:String): Response<List<StateResponse>>
}