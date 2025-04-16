package com.example.fooddream.model

import com.example.fooddream.models.OrderItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderItemUnitTest {

    private lateinit var orderItem: OrderItem

    @BeforeTest
    fun setUp() {
        orderItem = OrderItem(
            orderItemId = 1,
            productId = 101,
            orderId = 1001,
            quantity = 3,
            price = 9.99,
            itemName = "Cheeseburger"
        )
    }

    @Test
    fun `getOrderItemId should return correct value`() {
        assertEquals(1, orderItem.getOrderItemId())
    }

    @Test
    fun `setOrderItemId should update value`() {
        orderItem.setOrderItemId(2)
        assertEquals(2, orderItem.getOrderItemId())
    }

    @Test
    fun `getProductId should return correct value`() {
        assertEquals(101, orderItem.getProductId())
    }

    @Test
    fun `setProductId should update value`() {
        orderItem.setProductId(202)
        assertEquals(202, orderItem.getProductId())
    }

    @Test
    fun `getOrderId should return correct value`() {
        assertEquals(1001, orderItem.getOrderId())
    }

    @Test
    fun `setOrderId should update value`() {
        orderItem.setOrderId(2002)
        assertEquals(2002, orderItem.getOrderId())
    }

    @Test
    fun `getQuantity should return correct value`() {
        assertEquals(3, orderItem.getQuantity())
    }

    @Test
    fun `setQuantity should update value`() {
        orderItem.setQuantity(5)
        assertEquals(5, orderItem.getQuantity())
    }

    @Test
    fun `getPrice should return correct value`() {
        assertEquals(9.99, orderItem.getPrice())
    }

    @Test
    fun `setPrice should update value`() {
        orderItem.setPrice(12.49)
        assertEquals(12.49, orderItem.getPrice())
    }

    @Test
    fun `getItemName should return correct value`() {
        assertEquals("Cheeseburger", orderItem.getItemName())
    }

    @Test
    fun `setItemName should update value`() {
        orderItem.setItemName("Veggie Burger")
        assertEquals("Veggie Burger", orderItem.getItemName())
    }
}
