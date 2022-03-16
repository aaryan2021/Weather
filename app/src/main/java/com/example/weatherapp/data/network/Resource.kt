package com.example.weatherapp.data.network

data class Resource<out T>(val status:Status, val data:T?, val message:String?){
    companion object{
        fun <T> success(data: T): Resource<T> = Resource(status = Status.Success, data = data, message = null)

        fun <T> error (message:String,data:T?):Resource<T>{
            return Resource(Status.Failure,data,message)
        }

        fun <T> loading (data:T?):Resource<T>{
            return Resource(Status.Loading,data,null)
        }
    }
}