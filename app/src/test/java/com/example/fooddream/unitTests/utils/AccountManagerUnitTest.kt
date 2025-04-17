package com.example.fooddream.unitTests.utils

import android.util.Log
import com.example.fooddream.messengers.Errors
import org.junit.Test
import org.mindrot.jbcrypt.BCrypt

class AccountManagerUnitTest {

    private fun encryptPassword(password: String): String? {
        try {
            val salt = BCrypt.gensalt(12)
            val hashedPassword = BCrypt.hashpw(password, salt)
            return hashedPassword
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
        return null
    }

    @Test
    fun testEncryptPassword() {
        val password = "mySecurePassword"
        val hashedPassword = encryptPassword(password)

        assert(hashedPassword != null)

        assert(BCrypt.checkpw(password, hashedPassword))
    }
}
