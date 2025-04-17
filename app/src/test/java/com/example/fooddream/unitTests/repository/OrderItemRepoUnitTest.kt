package com.example.fooddream.unitTests.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.OrderItem
import com.example.fooddream.repositories.OrderItemRepository
import com.google.gson.Gson
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class OrderItemRepoUnitTest {

    private lateinit var mockActivity: AppCompatActivity
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var repository: OrderItemRepository

    private val gson = Gson()
    private val orderId = 42
    private val dummyItems = arrayListOf(
        OrderItem(1, 1, 1, 2, 3.00, "Burger"),
        OrderItem(2, 2, 2, 1, 2.00, "Cheese"),
    )

    @Before
    fun setup() {
        mockActivity = mockk()
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk()

        every { mockActivity.getSharedPreferences("orderItem_prefs", any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs

        repository = OrderItemRepository(mockActivity)
    }

    @Test
    fun `saveOrderItem should store JSON in SharedPreferences`() {
        repository.saveOrderItem(orderId, dummyItems)

        val expectedJson = gson.toJson(dummyItems)
        verify {
            mockEditor.putString("orderItem_$orderId", expectedJson)
            mockEditor.apply()
        }
    }

    @Test
    fun `getOrderItem should return deserialized list`() {
        val json = gson.toJson(dummyItems)
        every { mockPrefs.getString("orderItem_$orderId", null) } returns json

        val result = repository.getOrderItem(orderId)

        assertEquals(2, result.size)
        assertEquals("Burger", result[0].getItemName())
        assertEquals(2, result[0].getQuantity())
    }

    @Test
    fun `getOrderItem should return empty list if not found`() {
        every { mockPrefs.getString("orderItem_$orderId", null) } returns null

        val result = repository.getOrderItem(orderId)

        assertEquals(emptyList<OrderItem>(), result)
    }

    @Test
    fun `getOrderItems should return all matching order items`() {
        val json = gson.toJson(dummyItems)
        val prefsMap = mapOf(
            "orderItem_42" to json,
            "orderItem_42_extra" to json,
            "other_key" to "some_value"
        )
        every { mockPrefs.all } returns prefsMap

        val result = repository.getOrderItems(orderId)

        assertEquals(4, result.size) // 2 from each matching key
        assertEquals("Cheese", result[1].getItemName())
    }

    @Test
    fun `getOrderItems should return empty list if nothing matches`() {
        every { mockPrefs.all } returns mapOf(
            "orderItem_99" to gson.toJson(dummyItems)
        )

        val result = repository.getOrderItems(orderId)

        assertEquals(emptyList(), result)
    }
}
