package com.geocipher.app.interfaces

interface IMessageRepository {
    fun addMessage(encryptedMessage: String, key: String, latitude: Double, longitude: Double)
    fun retrieveMessage(key: String, latitude: Double, longitude: Double)
}