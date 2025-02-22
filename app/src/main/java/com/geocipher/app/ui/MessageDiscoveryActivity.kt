package com.geocipher.app.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.geocipher.app.R
import com.geocipher.app.interfaces.LocationUpdateListener
import com.geocipher.app.managers.EncryptionManagerSingleton
import com.geocipher.app.managers.LocationManagerSingleton
import com.geocipher.app.managers.MessageRepositorySingleton

class MessageDiscoveryActivity : AppCompatActivity(), LocationUpdateListener {
    private lateinit var textViewLat : TextView
    private lateinit var textViewLong : TextView
    private lateinit var recyclerView : RecyclerView
    private val locationManager = LocationManagerSingleton.getInstance(this)
    private val encryptionManager = EncryptionManagerSingleton.getInstance()
    private val messageRepository = MessageRepositorySingleton.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_message_discovery)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.messages_list)
        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)



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