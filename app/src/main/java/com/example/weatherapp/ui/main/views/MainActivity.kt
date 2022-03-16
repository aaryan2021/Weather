package com.example.weatherapp.ui.main.views

import android.Manifest
import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.weatherapp.NewAppWidget
import com.example.weatherapp.R
import com.example.weatherapp.data.network.Status
import com.example.weatherapp.ui.base.BaseActivity
import com.example.weatherapp.ui.main.viewmodel.WeatherViewModel
import com.example.weatherapp.utils.HelperMethods
import com.example.weatherapp.utils.Preferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var appwidgetId:Int=AppWidgetManager.INVALID_APPWIDGET_ID
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val viewModel by viewModels<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLocationPermission()

    }

    //CHECKING LOCATION PERMISSION
    private fun checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            requestLocationPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ))
        }else{
            getLastLocation()
        }
    }

    //OBJECT FOR LOCATION PERMISSION
    private val requestLocationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")

        }
        getLastLocation()
    }



    fun confirmConfiguration(data:String){
        var appWidgetManager:AppWidgetManager= AppWidgetManager.getInstance(this)
        val views = RemoteViews(this.packageName, R.layout.new_app_widget)
        views.setCharSequence(R.id.appwidget_text,"setText",data)
        appWidgetManager.updateAppWidget(appwidgetId,views)


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

    private fun fetchLatestWeather(state:String){
        viewModel.getLatestWeather(state).observe(this,
            {
                it?.let {
                        resource ->
                    when(resource.status){
                        Status.Success->{
                            progress?.dismissSweet()
                            it.data?.let {
                                address.text=it.name
                                status.text=it.weather!!.get(0).description
                                temp.text=HelperMethods.convertKelvinToCelcius(it.main!!.temp!!)+"°C"
                                temp_min.text="Min Tem: "+HelperMethods.convertKelvinToCelcius(it.main!!.temp_min!!)+"°C"
                                temp_max.text="Max Temp: "+HelperMethods.convertKelvinToCelcius(it.main!!.temp_max!!)+"°C"
                                sunrise.text=HelperMethods.getDate(it.sys!!.sunrise!!)
                                sunset.text=HelperMethods.getDate(it.sys!!.sunset!!)
                                updated_at.text="Updated at "+HelperMethods.getDate(it.dt!!)
                                wind.text=it.wind!!.speed!!+"km/h"
                                pressure.text=HelperMethods.convertToPaskal(it.main!!.pressure!!)+" pka"
                                humidity.text=it.main!!.humidity.toString()+"%"
                                Preferences(this).temp=HelperMethods.convertKelvinToCelcius(it.main!!.temp!!)+"°C"
                                Preferences(this).pressure="Pressure: "+HelperMethods.convertToPaskal(it.main!!.pressure!!)+" pka"
                                Preferences(this).time=HelperMethods.getDate(it.dt!!)
                                Preferences(this).status=it.weather!!.get(0).description
                                Preferences(this).humidity="Humidity: "+it.main!!.humidity.toString()+"%"
                                Preferences(this).location=it.name
                                confirmConfiguration(HelperMethods.convertKelvinToCelcius(it.main!!.temp!!)+"°C")

                            }


                        }
                        Status.Failure->{
                            progress?.dismissSweet()
                            it.message?.let { it1 -> alert?.showError(it1) }
                        }
                        Status.Loading->{
                            progress?.showSweetDialog()
                        }
                    }
                }
            })
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient!!.getLastLocation()
            .addOnSuccessListener(this, object : OnSuccessListener<Location?> {
                override fun onSuccess(location: Location?) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                    viewModel.getStateResponse(location.latitude,location.longitude).observe(this@MainActivity,
                        androidx.lifecycle.Observer {
                            it?.let {
                                    resource ->
                                when(resource.status){
                                    Status.Success->{
                                        progress?.dismissSweet()
                                        it.data?.let {
                                            Preferences(this@MainActivity).location=it!!.get(0).name
                                            fetchLatestWeather(it!!.get(0).name)
                                        }


                                    }
                                    Status.Failure->{
                                        progress?.dismissSweet()
                                        it.message?.let { it1 -> alert?.showError(it1) }
                                    }
                                    Status.Loading->{
                                        progress?.showSweetDialog()
                                    }
                                }
                            }

                        })
                    }
                }
            })
    }

}