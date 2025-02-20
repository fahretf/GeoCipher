package com.geocipher.app.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.geocipher.app.R
import com.geocipher.app.interfaces.LocationUpdateListener
import com.geocipher.app.managers.EncryptionManagerSingleton
import com.geocipher.app.managers.LocationManagerSingleton
import com.geocipher.app.managers.MessageRepositorySingleton

class MainActivity : AppCompatActivity(), LocationUpdateListener {

    private lateinit var textViewLat: TextView
    private lateinit var textViewLong: TextView
    private lateinit var uploadButton: Button
    private lateinit var viewMessagesButton : Button
    private val locationManager by lazy { LocationManagerSingleton.getInstance(this) }
    private val encryptionManager by lazy { EncryptionManagerSingleton.getInstance() }
    private val messageRepository by lazy { MessageRepositorySingleton.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button?>(R.id.upload_button).setOnClickListener(buttonUpload())
        findViewById<Button?>(R.id.view_messages_button).setOnClickListener(buttonViewMessages())
        viewMessagesButton = findViewById(R.id.view_messages_button)
        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)

        locationManager.startLocationTracking(this, this)


    }

    private fun buttonViewMessages(): View.OnClickListener? {
        TODO("Not yet implemented")

    }

    private fun buttonUpload(): View.OnClickListener? {
        TODO("Not yet implemented")
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