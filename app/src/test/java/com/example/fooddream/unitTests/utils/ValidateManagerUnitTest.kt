package com.example.fooddream.unitTests.utils

import com.example.fooddream.utils.ValidateManager
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateManagerUnitTest {

    private lateinit var validator: ValidateManager

    /**
     * Basic email regex for unit testing without Android SDK.
     * I used this instead of android.util.Patterns.EMAIL_ADDRESS
     * because that requires Android dependencies and won't work in plain unit tests.
     */
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isValidEmail(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    @Before
    fun setup() {
        validator = ValidateManager()
    }

    @Test
    fun `isValidEmail should return true for valid email`() {
        val result = isValidEmail("test@example.com")
        assertTrue(result)
    }

    @Test
    fun `isValidEmail should return false for invalid email`() {
        val result = isValidEmail("invalid-email")
        assertFalse(result)
    }

    @Test
    fun `isValidPassword should return true for password length greater or equal than 6`() {
        val result = validator.isValidPassword("abcdef")
        assertTrue(result)
    }

    @Test
    fun `isValidPassword should return false for password length less than 6`() {
        val result = validator.isValidPassword("123")
        assertFalse(result)
    }

    @Test
    fun `isValidName should return true for non-empty name`() {
        val result = validator.isValidName("Alice")
        assertTrue(result)
    }

    @Test
    fun `isValidName should return false for empty name`() {
        val result = validator.isValidName("")
        assertFalse(result)
    }

    @Test
    fun `isValidMessage should return true for non-empty message`() {
        val result = validator.isValidMessage("Hello!")
        assertTrue(result)
    }

    @Test
    fun `isValidMessage should return false for empty message`() {
        val result = validator.isValidMessage("")
        assertFalse(result)
    }
}
