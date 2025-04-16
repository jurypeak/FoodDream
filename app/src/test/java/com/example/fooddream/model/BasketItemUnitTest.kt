package com.example.fooddream.model

import com.example.fooddream.models.BasketItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BasketItemUnitTest {

    private lateinit var item: BasketItem

    @BeforeTest
    fun setUp() {
        item = BasketItem(
            productId = 101,
            basketId = 202,
            quantity = 3,
            price = 5.99,
            itemName = "Orange Juice"
        )
    }

    @Test
    fun `getProductId should return correct product ID`() {
        assertEquals(101, item.getProductId())
    }

    @Test
    fun `setProductId should update product ID`() {
        item.setProductId(111)
        assertEquals(111, item.getProductId())
    }

    @Test
    fun `getBasketId should return correct basket ID`() {
        assertEquals(202, item.getBasketId())
    }

    @Test
    fun `setBasketId should update basket ID`() {
        item.setBasketId(303)
        assertEquals(303, item.getBasketId())
    }

    @Test
    fun `getQuantity should return correct quantity`() {
        assertEquals(3, item.getQuantity())
    }

    @Test
    fun `setQuantity should update quantity`() {
        item.setQuantity(10)
        assertEquals(10, item.getQuantity())
    }

    @Test
    fun `getPrice should return correct price`() {
        assertEquals(5.99, item.getPrice())
    }

    @Test
    fun `setPrice should update price`() {
        item.setPrice(7.49)
        assertEquals(7.49, item.getPrice())
    }

    @Test
    fun `getItemName should return correct item name`() {
        assertEquals("Orange Juice", item.getItemName())
    }

    @Test
    fun `setItemName should update item name`() {
        item.setItemName("Apple Juice")
        assertEquals("Apple Juice", item.getItemName())
    }
}
