package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.PaymentRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PaymentRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: PaymentRepository
    private val gson = Gson()

    private val orderId = 101
    private val dummyPayments = arrayListOf(
        Payment(1, orderId, "Credit Card", "2023-12-1", 25.0),
        Payment(2, orderId, "Cash", "2023-12-2", 5.0),
    )
    private val json = gson.toJson(dummyPayments)

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("payment_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = PaymentRepository(mockActivity)
    }

    @Test
    fun `savePayments should serialize and save to SharedPreferences`() {
        repository.savePayments(orderId, dummyPayments)

        verify {
            mockEditor.putString("payments_$orderId", json)
            mockEditor.apply()
        }
    }

    @Test
    fun `getPayment should return first payment from list`() {
        every { mockPrefs.getString("payments_$orderId", null) } returns json

        val result = repository.getPayment(orderId)

        assertNotNull(result)
        assertEquals(1, result.getPaymentId())
        assertEquals("Credit Card", result.getPaymentMethod())
    }

    @Test
    fun `getPayment should return null if no data exists`() {
        every { mockPrefs.getString("payments_$orderId", null) } returns null

        val result = repository.getPayment(orderId)

        assertNull(result)
    }

    @Test
    fun `getPayments should return all payments from all keys`() {
        val map = mapOf(
            "payments_101" to json,
            "payments_102" to gson.toJson(arrayListOf(Payment(3, 102, "PayPal", "2023-3-12", 30.0)))
        )

        every { mockPrefs.all } returns map
        every { mockPrefs.getString(any(), null) } answers { map[firstArg()] }

        val result = repository.getPayments()

        assertEquals(3, result.size)
        assertEquals("Credit Card", result[0].getPaymentMethod())
        assertEquals("Cash", result[1].getPaymentMethod())
        assertEquals("PayPal", result[2].getPaymentMethod())
    }

    @Test
    fun `getPayments should return empty list if no payment keys exist`() {
        every { mockPrefs.all } returns mapOf("random_key" to "some_data")

        val result = repository.getPayments()

        assertEquals(0, result.size)
    }
}
