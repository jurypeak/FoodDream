package com.example.fooddream.unitTests.model

import com.example.fooddream.models.Checkout
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckoutUnitTest {

    private lateinit var checkout: Checkout

    @BeforeTest
    fun setUp() {
        checkout = Checkout(
            basketId = 1,
            email = "john@example.com",
            fName = "John",
            lName = "Doe",
            address = "123 Maple St",
            paymentMethod = "Credit Card",
            totalPrice = 29.99
        )
    }

    @Test
    fun `getBasketId should return correct value`() {
        assertEquals(1, checkout.getBasketId())
    }

    @Test
    fun `setBasketId should update basketId`() {
        checkout.setBasketId(2)
        assertEquals(2, checkout.getBasketId())
    }

    @Test
    fun `getEmail should return correct value`() {
        assertEquals("john@example.com", checkout.getEmail())
    }

    @Test
    fun `setEmail should update email`() {
        checkout.setEmail("jane@example.com")
        assertEquals("jane@example.com", checkout.getEmail())
    }

    @Test
    fun `getFName should return correct first name`() {
        assertEquals("John", checkout.getFName())
    }

    @Test
    fun `setFName should update first name`() {
        checkout.setFName("Jane")
        assertEquals("Jane", checkout.getFName())
    }

    @Test
    fun `getLName should return correct last name`() {
        assertEquals("Doe", checkout.getLName())
    }

    @Test
    fun `setLName should update last name`() {
        checkout.setLName("Smith")
        assertEquals("Smith", checkout.getLName())
    }

    @Test
    fun `getAddress should return correct address`() {
        assertEquals("123 Maple St", checkout.getAddress())
    }

    @Test
    fun `setAddress should update address`() {
        checkout.setAddress("456 Oak Ave")
        assertEquals("456 Oak Ave", checkout.getAddress())
    }

    @Test
    fun `getPaymentMethod should return correct payment method`() {
        assertEquals("Credit Card", checkout.getPaymentMethod())
    }

    @Test
    fun `setPaymentMethod should update payment method`() {
        checkout.setPaymentMethod("PayPal")
        assertEquals("PayPal", checkout.getPaymentMethod())
    }

    @Test
    fun `getTotalPrice should return correct total price`() {
        assertEquals(29.99, checkout.getTotalPrice())
    }

    @Test
    fun `setTotalPrice should update total price`() {
        checkout.setTotalPrice(39.99)
        assertEquals(39.99, checkout.getTotalPrice())
    }
}
