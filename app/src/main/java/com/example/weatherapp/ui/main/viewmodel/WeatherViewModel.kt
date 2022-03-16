package com.example.weatherapp.ui.main.viewmodel

import androidx.lifecycle.liveData
import com.example.weatherapp.data.network.Resource
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): BaseViewModel() {

    /*-------- CALLING LATEST Weather REPOSITORY-------------*/
    fun getLatestWeather(stateName:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getLatestWeather(stateName)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*-------- CALLING LATEST Weather REPOSITORY-------------*/
    fun getStateResponse(lat:Double,lon:Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getStateName(lat,lon)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}