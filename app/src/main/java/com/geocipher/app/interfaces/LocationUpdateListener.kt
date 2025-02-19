package com.geocipher.app.interfaces

interface LocationUpdateListener {
    fun onLocationUpdated(latitude: Double, longitude: Double)
    fun onLocationError(error: String)
}