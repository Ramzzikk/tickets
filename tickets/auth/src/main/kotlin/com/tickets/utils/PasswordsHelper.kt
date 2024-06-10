package com.tickets.utils

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PasswordsHelper(private val secretKey: String) {
    companion object {
        private const val DELIMITER = ':'

        private val random = SecureRandom()
        private val encoder = Base64.getEncoder()
        private val decoder = Base64.getDecoder()

        private fun generateSalt(): ByteArray {
            val salt = ByteArray(16)
            random.nextBytes(salt)
            return salt
        }

        private fun hashPassword(password: CharArray, salt: ByteArray, secretKey: String): ByteArray {
            val spec: KeySpec = PBEKeySpec(password, (salt + secretKey.toByteArray()), 120_000, 256)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            return factory.generateSecret(spec).encoded
        }

        private fun bytesToBase64(bytes: ByteArray): String = encoder.encodeToString(bytes)
        private fun base64ToBytes(base64: String): ByteArray = decoder.decode(base64)
    }

    fun hashPassword(password: String): String {
        val salt = generateSalt()
        val hash = hashPassword(password.toCharArray(), salt, secretKey)
        return "${bytesToBase64(salt)}$DELIMITER${bytesToBase64(hash)}"
    }

    fun validatePassword(password: String, storedPassword: String): Boolean {
        val parts = storedPassword.split(DELIMITER)
        if (parts.size != 2) {
            return false
        }

        val salt = base64ToBytes(parts[0])
        val storedHash = base64ToBytes(parts[1])

        val hash = hashPassword(password.toCharArray(), salt, secretKey)
        return hash.contentEquals(storedHash)
    }
}
