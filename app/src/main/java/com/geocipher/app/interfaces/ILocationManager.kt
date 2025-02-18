package com.geocipher.app.interfaces

import android.app.Activity

interface ILocationManager {
    fun getUserLocation(activity: Activity, listener: LocationUpdateListener)
    fun requestLocationPermission(activity: Activity)
    fun checkLocationPermission() : Boolean
}