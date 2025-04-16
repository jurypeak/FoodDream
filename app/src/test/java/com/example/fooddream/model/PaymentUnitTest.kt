package com.example.fooddream.model

import com.example.fooddream.models.Payment
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentUnitTest {

    private lateinit var payment: Payment

    @BeforeTest
    fun setUp() {
        payment = Payment(
            paymentId = 1,
            orderId = 1001,
            paymentMethod = "Credit Card",
            paymentDate = "2024-04-10",
            amount = 49.99
        )
    }

    @Test
    fun `getPaymentId should return correct value`() {
        assertEquals(1, payment.getPaymentId())
    }

    @Test
    fun `setPaymentId should update value`() {
        payment.setPaymentId(2)
        assertEquals(2, payment.getPaymentId())
    }

    @Test
    fun `getOrderId should return correct value`() {
        assertEquals(1001, payment.getOrderId())
    }

    @Test
    fun `setOrderId should update value`() {
        payment.setOrderId(2002)
        assertEquals(2002, payment.getOrderId())
    }

    @Test
    fun `getPaymentMethod should return correct value`() {
        assertEquals("Credit Card", payment.getPaymentMethod())
    }

    @Test
    fun `setPaymentMethod should update value`() {
        payment.setPaymentMethod("PayPal")
        assertEquals("PayPal", payment.getPaymentMethod())
    }

    @Test
    fun `getPaymentDate should return correct value`() {
        assertEquals("2024-04-10", payment.getPaymentDate())
    }

    @Test
    fun `setPaymentDate should update value`() {
        payment.setPaymentDate("2025-01-01")
        assertEquals("2025-01-01", payment.getPaymentDate())
    }

    @Test
    fun `getAmount should return correct value`() {
        assertEquals(49.99, payment.getAmount())
    }

    @Test
    fun `setAmount should update value`() {
        payment.setAmount(79.95)
        assertEquals(79.95, payment.getAmount())
    }
}
