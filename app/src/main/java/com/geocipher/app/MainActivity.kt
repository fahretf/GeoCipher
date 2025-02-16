package com.geocipher.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.location.Priority
import java.util.UUID
import com.google.crypto.tink.Config
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.Aead
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.JsonKeysetReader
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.subtle.Base64
import java.security.MessageDigest


private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var textViewLat: TextView
    private lateinit var textViewLong: TextView
    private lateinit var locationCallback: LocationCallback
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Toast.makeText(
                    this,
                    "You need to turn on Location services for this app to work.",
                    Toast.LENGTH_LONG
                ).show()
            }
            getUserLocation()
        } else {
            requestLocationPermission()
        }
        db = FirebaseFirestore.getInstance()
        AeadConfig.register()
        testFirestoreConnection()
        testEncryption()

    }

    private fun testFirestoreConnection() {
        val testDoc = hashMapOf(
            "message" to "MESSAGE",
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("testCollection")
            .add(testDoc)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    private fun addMessage(message: String, key: String, latitude: Double, longitude: Double) {
        val id = UUID.randomUUID().toString();
        val encryptedMessage = encryptMessage(message, key)

        val doc = hashMapOf(
            "id" to id,
            "encrypted_msg" to encryptedMessage,
            "latitude" to latitude,
            "longitude" to longitude,
        )
        db.collection("messages").add(doc)
    }

    private fun retrieveMessage(key: String, latitude: Double, longitude: Double) {
        TODO()
    }

    private fun encryptMessage(message: String, userKey: String): String {
        AeadConfig.register()

        val derivedKey =
            MessageDigest.getInstance("SHA-256").digest(userKey.toByteArray()).copyOf(32)

        val keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withBytes(derivedKey))

        val aead: Aead = keysetHandle.getPrimitive(Aead::class.java)

        val ciphertext: ByteArray = aead.encrypt(message.toByteArray(), null)

        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }


    fun decryptMessage(encryptedMessage: String, userKey: String): String {
        val derivedKey =
            MessageDigest.getInstance("SHA-256").digest(userKey.toByteArray()).copyOf(32)

        val keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withBytes(derivedKey))
        val aead: Aead = keysetHandle.getPrimitive(Aead::class.java)

        val ciphertext: ByteArray = Base64.decode(encryptedMessage, Base64.NO_WRAP)

        return String(aead.decrypt(ciphertext, null))
    }


    private fun getUserLocation() {
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

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission();
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                this,
                "You need to provide location access for the app to work.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Go to settings  to enable location.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun testEncryption() {
        val enkriptovanaPoruka = encryptMessage("Fahret", "qwert678")
        val dekriptovanaPoruka = decryptMessage(enkriptovanaPoruka, "qwert678")

        Log.d("CRYPT: ", "Enkriptovana poruka: $enkriptovanaPoruka")
        Log.d("CRYPT: ", "Dekriptovana poruka: $dekriptovanaPoruka")

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}