package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Address
import com.example.fooddream.repositories.AddressRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AddressRepoUnitTest {

    private lateinit var repository: AddressRepository
    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    private val gson = Gson()

    /**
     * Mocking the AppCompatActivity and SharedPreferences
     * to avoid using the actual Android framework
     * and to isolate the unit tests.
     * This allows me to test the AddressRepository
     * https://stackoverflow.com/questions/57150850/test-sharedpreferences-in-android
     */
    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk()
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("address_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = AddressRepository(mockActivity)
    }

    @Test
    fun `saveAddresses saves addresses to SharedPreferences`() {
        val addresses = listOf(Address(
            addressId = 1,
            orderId = 1,
            street = "123 Main St",
            postcode = "PH1492F",
            town = "Springfield"
        ))

        val orderId = 1
        val expectedJson = gson.toJson(addresses)

        repository.saveAddresses(orderId, addresses)

        verify {
            mockEditor.putString("address_$orderId", expectedJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getAddress returns first address from JSON`() {
        val addresses = listOf(Address(
            addressId = 1,
            orderId = 1,
            street = "123 Main St",
            postcode = "PH1492F",
            town = "Springfield"
        ))
        val orderId = 1
        val json = gson.toJson(addresses)

        every { mockPrefs.getString("address_$orderId", null) } returns json

        val result = repository.getAddress(orderId)

        assertEquals(addresses.first().getAddressId(), result?.getAddressId())
        assertEquals(addresses.first().getOrderId(), result?.getOrderId())
        assertEquals(addresses.first().getStreet(), result?.getStreet())
        assertEquals(addresses.first().getPostcode(), result?.getPostcode())
        assertEquals(addresses.first().getTown(), result?.getTown())
    }

    @Test
    fun `getAddress returns null if no data`() {
        val orderId = 2
        every { mockPrefs.getString("address_$orderId", null) } returns null

        val result = repository.getAddress(orderId)

        assertNull(result)
    }

    @Test
    fun `getAddresses returns address list from SharedPreferences`() {
        val addresses = listOf(Address(
            addressId = 1,
            orderId = 1,
            street = "123 Main St",
            postcode = "PH1492F",
            town = "Springfield"
        ))
        val orderId = 1
        val json = gson.toJson(addresses)

        every { mockPrefs.getString("address_$orderId", null) } returns json

        val result = repository.getAddresses(orderId)
        assertEquals(addresses.size, result.size)

        addresses.zip(result).forEach { (expected, actual) ->
            assertEquals(expected.getAddressId(), actual.getAddressId())
            assertEquals(expected.getOrderId(), actual.getOrderId())
            assertEquals(expected.getStreet(), actual.getStreet())
            assertEquals(expected.getPostcode(), actual.getPostcode())
            assertEquals(expected.getTown(), actual.getTown())
        }
    }

    @Test
    fun `getAddresses returns empty list if no data`() {
        val orderId = 3
        every { mockPrefs.getString("address_$orderId", null) } returns null

        val result = repository.getAddresses(orderId)

        assertEquals(emptyList(), result)
    }
}
