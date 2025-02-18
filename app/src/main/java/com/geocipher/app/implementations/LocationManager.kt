package com.geocipher.app.implementations

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.geocipher.app.interfaces.ILocationManager
import com.geocipher.app.interfaces.LocationUpdateListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

class LocationManager(private val context: Context) : ILocationManager {
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun getUserLocation(activity: Activity, listener: LocationUpdateListener) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location == null) {
                    Toast.makeText(
                        context,
                        "No available GPS data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission(activity);
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun requestLocationPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                activity,
                "You need to provide location access for the app to work.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun checkLocationPermission(): Boolean {
        TODO("Not yet implemented")
    }
}