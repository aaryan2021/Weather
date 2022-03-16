package com.example.weatherapp.data.network

import com.example.weatherapp.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.errorBody()?.toString()
            val message = StringBuilder()
            error?.let{
                try{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    message.append(jsonObj.getString("message"))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }

}