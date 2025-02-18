package com.geocipher.app.interfaces

interface ILocationManager {
    fun getUserLocation()
    fun requestLocationPermission()
    fun checkLocationPermission(): Boolean
}