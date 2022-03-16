package com.example.weatherapp.data.network

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.utils.HelperMethods
import com.example.weatherapp.utils.Preferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.ComponentName
import android.util.Log
import com.example.weatherapp.NewAppWidget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkManger():Service() {

    @Inject
    lateinit var apiInterface: ApiService
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val allWidgetIds = intent?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,0)
        try {
                var url:String="data/2.5/weather?q="+Preferences(this).location!!+"&appid="+ BuildConfig.API_KEY
                val call: Call<WeatherResponse> = apiInterface!!.ProvideWeather(url)
                call.enqueue(object : Callback<WeatherResponse?> {
                    override fun onResponse(call: Call<WeatherResponse?>?, response: Response<WeatherResponse?>) {
                        Preferences(applicationContext).temp= HelperMethods.convertKelvinToCelcius(response.body()!!.main!!.temp!!)+"°C"
                        Preferences(applicationContext).pressure="Pressure:"+HelperMethods.convertToPaskal(response.body()!!.main!!.pressure!!)+" pka"
                        Preferences(applicationContext).time=HelperMethods.getDate(response.body()!!.dt!!)
                        Preferences(applicationContext).status=response.body()!!.weather!!.get(0).description
                        Preferences(applicationContext).humidity="Humidity: "+response.body()!!.main!!.humidity.toString()+"%"
                        Preferences(applicationContext).location=response.body()!!.name
                        var appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(applicationContext)
                        val views = RemoteViews(applicationContext.packageName, R.layout.new_app_widget)
                        views.setCharSequence(R.id.appwidget_text,"setText",HelperMethods.convertKelvinToCelcius(response.body()!!.main!!.temp!!)+"°C")
                        if (allWidgetIds != null) {
                            appWidgetManager.updateAppWidget(allWidgetIds,views)
                        }

                        val intent = Intent(applicationContext, NewAppWidget::class.java)
                        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
                        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                            ComponentName(
                                application,
                                NewAppWidget::class.java
                            )
                        )
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                        sendBroadcast(intent)
                    }
                    override fun onFailure(call: Call<WeatherResponse?>, t: Throwable?) {
                        call.cancel()
                    }
                })
        } catch (exception: Exception) {
            Log.e("data",exception.toString())
        }
        return START_REDELIVER_INTENT

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}