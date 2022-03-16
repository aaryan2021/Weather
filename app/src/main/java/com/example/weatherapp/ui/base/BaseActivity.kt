package com.example.weatherapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.utils.CustomAlert
import com.example.weatherapp.utils.CustomProgressDialog

open class BaseActivity : AppCompatActivity() {
    var progress: CustomProgressDialog? = null
    var alert: CustomAlert? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = CustomProgressDialog(this)
        alert = CustomAlert(this)
    }

}