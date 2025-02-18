package com.geocipher.app.implementations

import com.geocipher.app.interfaces.IEncryptionManager

class EncryptionManager : IEncryptionManager {
    override fun encryptMessage(plaintext: String, userKey: String): String {
        TODO("Not yet implemented")
    }

    override fun decryptMessage(encryptedMessage: String, userKey: String) {
        TODO("Not yet implemented")
    }

    override fun deriveKey(userKey: String): ByteArray {
        TODO("Not yet implemented")
    }

}