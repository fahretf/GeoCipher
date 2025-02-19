package com.geocipher.app.implementations

import com.geocipher.app.interfaces.IMessageRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class MessageRepository : IMessageRepository {
    private lateinit var db: FirebaseFirestore

    init {
        db = FirebaseFirestore.getInstance()
    }

    override fun addMessage(encryptedMessage: String, key: String, latitude: Double, longitude: Double) {
        val id = UUID.randomUUID().toString();
        val doc = hashMapOf(
            "id" to id,
            "encrypted_msg" to encryptedMessage,
            "latitude" to latitude,
            "longitude" to longitude,
        )
        db.collection("messages").add(doc)
    }

    override fun retrieveMessage(key: String, latitude: Double, longitude: Double) {
        TODO("Not yet implemented")
    }

    override fun testConnection(): Task<DocumentReference> {
        return db.collection("example").add(hashMapOf(
            "name" to "Fahret",
            "code" to "FFFFFF"
        ))
    }
}