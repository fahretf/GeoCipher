package com.geocipher.app.interfaces

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface IMessageRepository {
    fun addMessage(encryptedMessage: String, key: String, latitude: Double, longitude: Double): Task<DocumentReference>
    fun retrieveMessage(latitude: Double, longitude: Double)
    fun testConnection(): Task<DocumentReference>
}