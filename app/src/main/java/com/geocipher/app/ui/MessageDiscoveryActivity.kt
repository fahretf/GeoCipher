package com.geocipher.app.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geocipher.app.R
import com.geocipher.app.interfaces.LocationUpdateListener
import com.geocipher.app.managers.EncryptionManagerSingleton
import com.geocipher.app.managers.LocationManagerSingleton
import com.geocipher.app.managers.MessageRepositorySingleton
import com.geocipher.app.models.Message

class MessageDiscoveryActivity : AppCompatActivity(), LocationUpdateListener {
    private lateinit var textViewLat : TextView
    private lateinit var textViewLong : TextView
    private lateinit var recyclerView : RecyclerView
    private lateinit var messageAdapter : MessageAdapter
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
        findViewById<Button>(R.id.scan_button).setOnClickListener { buttonScan() }

        recyclerView = findViewById(R.id.messages_list)
        textViewLat = findViewById(R.id.latitude)
        textViewLong = findViewById(R.id.longitude)

        messageAdapter = MessageAdapter { message ->
            showDecryptDialog(message)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        locationManager.startLocationTracking(this, this)

    }

    private fun buttonScan() {
        messageRepository.retrieveMessage(
            textViewLat.text.toString().toDouble(),
            textViewLong.text.toString().toDouble()
        ).addOnSuccessListener { querySnapshot ->
            val messagesList = querySnapshot.documents.map { doc ->
                Message(
                    id = doc.getString("id") ?: "",
                    encryptedMessage = doc.getString("encrypted_msg") ?: "",
                    timestamp = doc.getTimestamp("timestamp")!!,
                    latitude = doc.getDouble("latitude") ?: 0.0,
                    longitude = doc.getDouble("longitude") ?: 0.0
                )
            }
            messageAdapter.updateMessages(messagesList)
        }.addOnFailureListener { e ->
            Log.i("SCANNING", e.toString())
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

    private fun showDecryptDialog(message: Message) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_decrypt)

        val keyInput = dialog.findViewById<EditText>(R.id.decrypt_key_input)
        val decryptButton = dialog.findViewById<Button>(R.id.decrypt_button)

        decryptButton.setOnClickListener {
            val key = keyInput.text.toString()
            val decryptedMessage = encryptionManager.decryptMessage(message.encryptedMessage, key)

            if (decryptedMessage != null) {
                Toast.makeText(this, "Decrypted: $decryptedMessage", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Wrong key or invalid message", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}