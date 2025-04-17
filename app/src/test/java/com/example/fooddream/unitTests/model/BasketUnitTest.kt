package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Basket
import com.example.fooddream.models.BasketItem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BasketUnitTest {

    private lateinit var basket: Basket
    private lateinit var item1: BasketItem
    private lateinit var item2: BasketItem

    @BeforeTest
    fun setUp() {
        item1 = BasketItem(1, 2, 1, 2.99, "Pear")
        item2 = BasketItem(2, 2, 1, 1.49, "Apple")

        basket = Basket(
            totalPrice = 4.48,
            accountId = 101,
            guestId = 0,
            basketId = 10,
            totalItems = 2
        )
    }

    @Test
    fun `getTotalPrice should return correct total price`() {
        assertEquals(4.48, basket.getTotalPrice())
    }

    @Test
    fun `setTotalPrice should update total price`() {
        basket.setTotalPrice(9.99)
        assertEquals(9.99, basket.getTotalPrice())
    }

    @Test
    fun `getAccountId should return correct account ID`() {
        assertEquals(101, basket.getAccountId())
    }

    @Test
    fun `setAccountId should update account ID`() {
        basket.setAccountId(202)
        assertEquals(202, basket.getAccountId())
    }

    @Test
    fun `getGuestId should return correct guest ID`() {
        assertEquals(0, basket.getGuestId())
    }

    @Test
    fun `setGuestId should update guest ID`() {
        basket.setGuestId(303)
        assertEquals(303, basket.getGuestId())
    }

    @Test
    fun `basketId should return correct basket ID`() {
        assertEquals(10, basket.basketId())
    }

    @Test
    fun `setBasketId should update basket ID`() {
        basket.setBasketId(20)
        assertEquals(20, basket.basketId())
    }

    @Test
    fun `totalItems should return correct total items`() {
        assertEquals(2, basket.totalItems())
    }

    @Test
    fun `setTotalItems should update total items`() {
        basket.setTotalItems(5)
        assertEquals(5, basket.totalItems())
    }

    @Test
    fun `addItems should add item to basket`() {
        basket.addItems(item1)
        basket.addItems(item2)
        val items = basket.getItems()
        assertEquals(2, items.size)
        assertEquals("Pear", items[0].getItemName())
        assertEquals("Apple", items[1].getItemName())
    }
}
