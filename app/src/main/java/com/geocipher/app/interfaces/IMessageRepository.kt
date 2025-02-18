package com.geocipher.app.interfaces

interface IMessageRepository {
    fun addMessage(message: String, key: String, latitude: Double, longitude: Double)
}