package com.example.weatherapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.lifecycle.liveData
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.ApiCLient
import com.example.weatherapp.data.network.ApiService
import com.example.weatherapp.data.network.Resource
import com.example.weatherapp.data.network.WorkManger
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.main.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.main.views.MainActivity
import com.example.weatherapp.utils.HelperMethods
import com.example.weatherapp.utils.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class NewAppWidget : AppWidgetProvider() {

    var data:WeatherResponse?=null


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {}

    override fun onDisabled(context: Context) {}
}

  fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      val widgetText = Preferences(context).temp
      val views = RemoteViews(context.packageName, R.layout.new_app_widget)
      views.setTextViewText(R.id.appwidget_text, widgetText)
      views.setTextViewText(R.id.cityname, Preferences(context).location)
      views.setTextViewText(R.id.date, Preferences(context).time)
      views.setTextViewText(R.id.humidity, Preferences(context).humidity)
      views.setTextViewText(R.id.status, Preferences(context).status)
      views.setTextViewText(R.id.pressure, Preferences(context).pressure)
      var intent=Intent(context,MainActivity::class.java)
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId)
      var pendingIntent=PendingIntent.getActivity(context,0,intent,0)
      views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent)

      val startServiceIntent = Intent(
          context,
          WorkManger::class.java
      )
      var pendingIntents=PendingIntent.getService(context,0,startServiceIntent,0)
      views.setOnClickPendingIntent(R.id.refresh,pendingIntents)
      appWidgetManager.updateAppWidget(appWidgetId, views)

}
