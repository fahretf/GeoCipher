package com.geocipher.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(checkLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun getUserLocation() {
        TODO("Not yet implemented")
    }

    private fun requestLocationPermission() {
        TODO("Not yet implemented")
    }

    private fun checkLocationPermission(): Boolean {
        TODO("Not yet implemented")
    }
}