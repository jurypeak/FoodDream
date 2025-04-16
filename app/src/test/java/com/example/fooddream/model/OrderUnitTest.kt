package com.example.fooddream.model

import com.example.fooddream.models.Order
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderUnitTest {

    private lateinit var order: Order

    @BeforeTest
    fun setUp() {
        order = Order(
            fName = "Alice",
            lName = "Smith",
            email = "alice@example.com",
            accountId = 123,
            orderId = 1001,
            orderDate = "2024-04-10"
        )
    }

    @Test
    fun `getFName should return correct value`() {
        assertEquals("Alice", order.getFName())
    }

    @Test
    fun `setFName should update value`() {
        order.setFName("Bob")
        assertEquals("Bob", order.getFName())
    }

    @Test
    fun `getLName should return correct value`() {
        assertEquals("Smith", order.getLName())
    }

    @Test
    fun `setLName should update value`() {
        order.setLName("Johnson")
        assertEquals("Johnson", order.getLName())
    }

    @Test
    fun `getEmail should return correct value`() {
        assertEquals("alice@example.com", order.getEmail())
    }

    @Test
    fun `setEmail should update value`() {
        order.setEmail("bob@example.com")
        assertEquals("bob@example.com", order.getEmail())
    }

    @Test
    fun `getAccountId should return correct value`() {
        assertEquals(123, order.getAccountId())
    }

    @Test
    fun `setAccountId should update value`() {
        order.setAccountId(456)
        assertEquals(456, order.getAccountId())
    }

    @Test
    fun `setAccountId should accept null`() {
        order.setAccountId(null)
        assertNull(order.getAccountId())
    }

    @Test
    fun `getOrderId should return correct value`() {
        assertEquals(1001, order.getOrderId())
    }

    @Test
    fun `setOrderId should update value`() {
        order.setOrderId(2002)
        assertEquals(2002, order.getOrderId())
    }

    @Test
    fun `getOrderDate should return correct value`() {
        assertEquals("2024-04-10", order.getOrderDate())
    }

    @Test
    fun `setOrderDate should update value`() {
        order.setOrderDate("2025-01-01")
        assertEquals("2025-01-01", order.getOrderDate())
    }
}
