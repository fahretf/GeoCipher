package com.geocipher.app.implementations

import android.widget.Toast
import com.geocipher.app.interfaces.ILocationManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

class LocationManager : ILocationManager {
    private lateinit var locationCallback: LocationCallback

    override fun getUserLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "No available GPS data",
                        Toast.LENGTH_LONG
                    ).show()
                }

                textViewLat.text = location?.latitude.toString()
                textViewLong.text = location?.longitude.toString()
            }
    }

    override fun requestLocationPermission() {
        TODO("Not yet implemented")
    }

    override fun checkLocationPermission(): Boolean {
        TODO("Not yet implemented")
    }
}