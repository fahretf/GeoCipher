package com.geocipher.app.implementations

import android.util.Base64
import android.util.Log
import com.geocipher.app.interfaces.IEncryptionManager
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptionManager : IEncryptionManager {
    override fun encryptMessage(plaintext: String, userKey: String): String {
        val secretKey = SecretKeySpec(deriveKey(userKey), "AES")
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val ciphertext = cipher.doFinal(plaintext.toByteArray())

        val combined = ByteArray(iv.size + ciphertext.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(ciphertext, 0, combined, iv.size, ciphertext.size)

        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    override fun decryptMessage(encryptedMessage: String, userKey: String): String? {
        return try {
            val combined = Base64.decode(encryptedMessage, Base64.NO_WRAP)
            val ivSize = 12
            val iv = combined.copyOfRange(0, ivSize)
            val ciphertext = combined.copyOfRange(ivSize, combined.size)

            val secretKey = SecretKeySpec(deriveKey(userKey), "AES")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

            val decrypted = cipher.doFinal(ciphertext)
            String(decrypted)
        } catch (e: Exception) {
            Log.e("CryptoError", "Decryption failed because: ${e.message}")
            null
        }
    }

    override fun deriveKey(userKey: String): ByteArray {
        val salt = "someSalt".toByteArray()
        val iterations = 100_000
        val keyLength = 256

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(userKey.toCharArray(), salt, iterations, keyLength)
        return factory.generateSecret(spec).encoded
    }

}