package com.geocipher.app.managers

import android.content.Context
import com.geocipher.app.implementations.LocationManager
import com.geocipher.app.interfaces.ILocationManager

object LocationManagerSingleton {
    private var instance: LocationManager? = null

    fun getInstance(context: Context): LocationManager {
        if (instance == null) {
            instance = LocationManager(context.applicationContext)
        }
        return instance!!
    }
}