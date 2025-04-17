package com.example.fooddream.unitTests.repository

import CustomerRepository
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Customer
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CustomerRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: CustomerRepository

    private val gson = Gson()
    private val dummyCustomer = Customer(
        "John", "Doe", "john.doe@example.com", 1, 1, "password123"
    )

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk()
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("app_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = CustomerRepository(mockActivity)
    }

    @Test
    fun `saveCustomer should store JSON in SharedPreferences`() {
        repository.saveCustomer(dummyCustomer)

        val expectedJson = gson.toJson(dummyCustomer)
        verify {
            mockEditor.putString("customer_key", expectedJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getCustomer should return deserialized customer`() {
        val customerJson = gson.toJson(dummyCustomer)
        every { mockPrefs.getString("customer_key", null) } returns customerJson

        val result = repository.getCustomer()

        assertEquals(dummyCustomer.getEmail(), result?.getEmail())
        assertEquals(dummyCustomer.getFName(), result?.getFName())
    }

    @Test
    fun `getCustomer should return null if not found`() {
        every { mockPrefs.getString("customer_key", null) } returns null

        val result = repository.getCustomer()

        assertNull(result)
    }

    @Test
    fun `deleteCustomer should remove customer_key from SharedPreferences`() {
        repository.deleteCustomer()

        verify {
            mockEditor.remove("customer_key")
            mockEditor.apply()
        }
    }

    @Test
    fun `updateCustomer should modify fields and save back`() {
        val originalJson = gson.toJson(dummyCustomer)
        every { mockPrefs.getString("customer_key", null) } returns originalJson

        val newFName = "Jane"
        val newLName = "Smith"
        val newEmail = "jane.smith@example.com"
        val newPassword = "newpass456"

        val jsonSlot = slot<String>()
        every { mockEditor.putString("customer_key", capture(jsonSlot)) } returns mockEditor

        repository.updateCustomer(newFName, newLName, newEmail, newPassword)

        verify { mockEditor.apply() }

        val updatedCustomer = gson.fromJson(jsonSlot.captured, Customer::class.java)

        assertEquals(newFName, updatedCustomer.getFName())
        assertEquals(newLName, updatedCustomer.getLName())
        assertEquals(newEmail, updatedCustomer.getEmail())
        assertEquals(newPassword, updatedCustomer.getPassword())
    }
}

