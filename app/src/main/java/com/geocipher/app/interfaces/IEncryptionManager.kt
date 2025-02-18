package com.geocipher.app.interfaces

interface IEncryptionManager {
    fun encryptMessage(plaintext: String, userKey: String): String
    fun decryptMessage(encryptedMessage: String, userKey: String): String?
    fun deriveKey(userKey: String): ByteArray
}