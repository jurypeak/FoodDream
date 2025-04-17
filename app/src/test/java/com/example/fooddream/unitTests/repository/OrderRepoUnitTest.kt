package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Order
import com.example.fooddream.repositories.OrderRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class OrderRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: OrderRepository
    private val gson = Gson()

    private val dummyOrder = Order(
        fName = "John",
        lName = "Doe",
        email = "john@example.com",
        accountId = 12,
        orderId = 101,
        orderDate = "2023-10-01",
    )
    private val accountId = 12
    private val orderKey = "order_${accountId}_${dummyOrder.getOrderId()}"

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("app_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = OrderRepository(mockActivity)
    }

    @Test
    fun `saveOrder should serialize and store the order`() {
        repository.saveOrder(accountId, dummyOrder)

        val expectedJson = gson.toJson(dummyOrder)
        verify {
            mockEditor.putString(orderKey, expectedJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getOrder should deserialize and return order if present`() {
        val json = gson.toJson(dummyOrder)
        every { mockPrefs.getString(orderKey, null) } returns json

        val result = repository.getOrder(dummyOrder.getOrderId(), accountId)

        assertNotNull(result)
        assertEquals(dummyOrder.getOrderId(), result.getOrderId())
    }

    @Test
    fun `getOrder should return null if order not found`() {
        every { mockPrefs.getString(orderKey, null) } returns null

        val result = repository.getOrder(dummyOrder.getOrderId(), accountId)

        assertNull(result)
    }

    @Test
    fun `getAllOrders should return all stored orders`() {
        val json = gson.toJson(dummyOrder)
        val keys = mapOf(
            "order_12_101" to json,
            "order_15_202" to json,
            "something_else" to "junk"
        )
        every { mockPrefs.all } returns keys
        every { mockPrefs.getString(any(), any()) } answers { keys[firstArg()] }

        val result = repository.getAllOrders()

        assertEquals(2, result.size)
        assertEquals(dummyOrder.getOrderId(), result[0].getOrderId())
    }

    @Test
    fun `numberOfOrders should count only order keys`() {
        val allKeys = mapOf(
            "order_1_1" to "data1",
            "order_1_2" to "data2",
            "non_order_key" to "data3"
        )
        every { mockPrefs.all } returns allKeys

        val count = repository.numberOfOrders()

        assertEquals(2, count)
    }

    @Test
    fun `removeOrder should remove correct key`() {
        repository.removeOrder(dummyOrder.getOrderId())

        verify {
            mockEditor.remove("order_${dummyOrder.getOrderId()}")
            mockEditor.apply()
        }
    }
}
