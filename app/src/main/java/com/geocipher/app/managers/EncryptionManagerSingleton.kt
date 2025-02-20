package com.geocipher.app.managers

import android.content.Context
import com.geocipher.app.implementations.EncryptionManager

object EncryptionManagerSingleton {
    private var instance: EncryptionManager? = null

    fun getInstance() : EncryptionManager {
        if(instance == null) {
            instance = EncryptionManager()
        }
        return instance!!
    }
}