package com.geocipher.app.implementations

import com.geocipher.app.interfaces.IMessageRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

val COORDINATE_RANGE = 0.00045
class MessageRepository : IMessageRepository {
    private lateinit var db: FirebaseFirestore

    init {
        db = FirebaseFirestore.getInstance()
    }

    override fun addMessage(encryptedMessage: String, key: String, latitude: Double, longitude: Double): Task<DocumentReference> {
        val id = UUID.randomUUID().toString();
        val doc = hashMapOf(
            "id" to id,
            "encrypted_msg" to encryptedMessage,
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to Timestamp(Date())
        )
        db.collection("messages").add(doc)
    }

    override fun retrieveMessage(latitude: Double, longitude: Double) {
        db.collection("messages")
            .whereLessThanOrEqualTo("latitude", latitude + COORDINATE_RANGE)
            .whereLessThanOrEqualTo("longitude", longitude + COORDINATE_RANGE)
            .whereGreaterThanOrEqualTo("latitude", latitude - COORDINATE_RANGE)
            .whereGreaterThanOrEqualTo("longitude", longitude - COORDINATE_RANGE).get()
    }

    override fun testConnection(): Task<DocumentReference> {
        return db.collection("example").add(hashMapOf(
            "name" to "Fahret",
            "code" to "FFFFFF"
        ))
    }
}