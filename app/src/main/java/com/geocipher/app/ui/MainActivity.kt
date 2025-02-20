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
import android.content.Intent
import android.widget.EditText

class MainActivity : AppCompatActivity(), LocationUpdateListener {

    private lateinit var textViewLat: TextView
    private lateinit var textViewLong: TextView
    private lateinit var messageInput: EditText
    private lateinit var keyInput: EditText
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

        findViewById<Button>(R.id.upload_button).setOnClickListener { buttonUpload() }
        findViewById<Button>(R.id.view_messages_button).setOnClickListener { buttonViewMessages() }
        viewMessagesButton = findViewById(R.id.view_messages_button)
        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)
        messageInput = findViewById(R.id.message_input)
        keyInput = findViewById(R.id.key_input)

        locationManager.startLocationTracking(this, this)


    }

    private fun buttonViewMessages() {
        val intent = Intent(this, MessageDiscoveryActivity::class.java)
        startActivity(intent)
    }

    private fun buttonUpload() {
        messageRepository.addMessage(
            encryptionManager.encryptMessage(
                messageInput.text.toString(),
                keyInput.text.toString()
            ),
            keyInput.text.toString(),
            textViewLat.text.toString().toDouble(),
            textViewLong.text.toString().toDouble()

        ).addOnSuccessListener {
            Toast.makeText(this,
                "Successfully uploaded the message!",
                Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this,
                "Your message didn't get uploaded due to an error.",
                Toast.LENGTH_LONG).show()
        }
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