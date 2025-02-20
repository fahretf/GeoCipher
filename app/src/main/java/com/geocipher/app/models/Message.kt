package com.geocipher.app.models

import com.google.firebase.Timestamp

data class Message(
    val id: String,
    val encryptedMessage: String,
    val timestamp: Timestamp,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", Timestamp.now(), 0.0, 0.0)
}