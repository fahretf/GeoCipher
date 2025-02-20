package com.geocipher.app.managers

import com.geocipher.app.implementations.MessageRepository

object MessageRepositorySingleton {
    private var instance : MessageRepository? = null

    fun getInstance(): MessageRepository {
        if(instance == null) {
            instance = MessageRepository()
        }
        return instance!!
    }
}