package com.example.fooddream.unitTests.utils

import android.util.Log
import com.example.fooddream.messengers.Errors
import org.mindrot.jbcrypt.BCrypt
import kotlin.test.Test

class AuthenticationManagerUnitTest {

    private fun verifyPassword(
        password: String,
        hashedPassword: String
    ): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (error: Errors.ComparingException) {
            Log.d("Password Comparing Error", "$error")
            false
        }
    }

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
    fun testVerifyPassword() {
        val password = "mySecurePassword"
        val hashedPassword = encryptPassword(password)

        assert(hashedPassword != null)

        val isVerified = verifyPassword(password, hashedPassword!!)
        assert(isVerified)
    }

    @Test
    fun testVerifyPasswordWithIncorrectPassword() {
        val password = "mySecurePassword"
        val hashedPassword = encryptPassword(password)

        assert(hashedPassword != null)

        val isVerified = verifyPassword("wrongPassword", hashedPassword!!)
        assert(!isVerified)
    }

    @Test
    fun testVerifyPasswordWithEmptyPassword() {
        val password = ""
        val hashedPassword = encryptPassword("mySecurePassword")

        assert(hashedPassword != null)

        val isVerified = verifyPassword(password, hashedPassword!!)
        assert(!isVerified)
    }
}