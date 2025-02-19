package com.geocipher.app.ui

import com.geocipher.app.implementations.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.geocipher.app.R
import com.geocipher.app.implementations.EncryptionManager
import com.geocipher.app.implementations.MessageRepository
import com.geocipher.app.interfaces.LocationUpdateListener

class MainActivity : AppCompatActivity(), LocationUpdateListener {

    private lateinit var textViewLat: TextView
    private lateinit var textViewLong: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var encryptionManager: EncryptionManager
    private lateinit var messageRepository: MessageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        locationManager = LocationManager(this)
        encryptionManager = EncryptionManager()
        messageRepository = MessageRepository()


        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)

        val enkriptovana = encryptionManager.encryptMessage(
            "Fahret",
            "qwert678"
        )

        val dekriptovana = encryptionManager.decryptMessage(enkriptovana, "qwert678")

        Log.i("MainActivity", "aaaa")
        Log.i("MainActivity", "Enkriptovana: $enkriptovana")
        Log.i("MainActivity", "Dekriptovana: $dekriptovana")

        locationManager.startLocationTracking(this, this)

    }

    override fun onLocationUpdated(latitude: Double, longitude: Double) {
        textViewLat.text = latitude.toString()
        textViewLong.text = longitude.toString()
    }

    override fun onLocationError(error: String) {
        Toast
            .makeText(this, error, Toast.LENGTH_LONG)
            .show()
    }

}