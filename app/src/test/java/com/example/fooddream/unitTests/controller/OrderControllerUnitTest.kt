package com.example.fooddream.unitTests.controller

import android.util.Log
import junit.framework.TestCase.assertTrue
import kotlin.test.Test

class OrderControllerUnitTest {

    private fun isValidPostcode(postcode: String): Boolean {
        try {
            val cleanedPostcode = postcode.replace("\\s".toRegex(), "")

            val regex = Regex("^([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([AZa-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))[0-9][A-Za-z]{2})\$")
            return regex.matches(cleanedPostcode)
        } catch (e: Exception) {
            Log.e("OrderController", "Error validating postcode: ${e.message}")
            return false
        }
    }

    @Test
    fun `isValidPostcode should return true for valid postcode`() {
        val result = isValidPostcode("SW1A 1AA")
        assertTrue(result)
    }

    @Test
    fun `isValidPostcode should return false for invalid postcode`() {
        val result = isValidPostcode("INVALID_POSTCODE")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for empty postcode`() {
        val result = isValidPostcode("")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for null postcode`() {
        val result = isValidPostcode("null")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for postcode with only spaces`() {
        val result = isValidPostcode("   ")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for postcode with special characters`() {
        val result = isValidPostcode("SW1A@1AA")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for postcode with too many characters`() {
        val result = isValidPostcode("SW1A 1AA 123")
        assertTrue(!result)
    }

    @Test
    fun `isValidPostcode should return false for postcode with too few characters`() {
        val result = isValidPostcode("SW1")
        assertTrue(!result)
    }
}