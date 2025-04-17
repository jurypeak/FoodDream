package com.example.fooddream.unitTests.viewController

import com.example.fooddream.models.Order
import com.example.fooddream.models.Payment
import java.text.NumberFormat
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountViewControllerUnitTest {

    /**
     * This is a mock function from AccountViewController that checks if orders exist.
     * It returns the latest order summary if orders are present, otherwise returns no orders.
     * This to simulate the behavior of the actual function in a testable manner.
     * And easier to test without needing to test the UI which is difficult and time-consuming.
     */
    data class OrderSummary(val orderId: String, val orderDate: String, val orderAmount: String)

    fun checkIfOrdersExistTest(
        payments: List<Payment>,
        orders: List<Order>,
        currencyFormat: NumberFormat
    ): OrderSummary {
        return if (orders.isNotEmpty()) {
            val latestOrder = orders.last()
            val latestPayment = payments.lastOrNull { it.getOrderId() == latestOrder.getOrderId() }

            val orderId = "Order #${latestOrder.getOrderId()}"
            val orderDate = latestOrder.getOrderDate()
            val orderAmount = latestPayment?.let { currencyFormat.format(it.getAmount()) } ?: ""

            OrderSummary(orderId, orderDate, orderAmount)
        } else {
            OrderSummary("No Orders", "", "")
        }
    }

    @Test
    fun `checkIfOrdersExistTest returns latest order correctly`() {
        val currencyFormat = NumberFormat.getCurrencyInstance()

        val orders = listOf(Order(
            fName = "Jane",
            lName = "Doe",
            email = "jane@example.com",
            accountId = 1,
            orderId = 456,
            orderDate = "2023-12-01"
        ))

        val payments = listOf(Payment(
            paymentId = 1,
            orderId = 456,
            paymentMethod = "PayPal",
            paymentDate = "2023-12-01",
            amount = 99.99
        ))

        val summary = checkIfOrdersExistTest(payments, orders, currencyFormat)

        assertEquals("Order #456", summary.orderId)
        assertEquals("2023-12-01", summary.orderDate)
        assertEquals(currencyFormat.format(99.99), summary.orderAmount)
    }

    @Test
    fun `checkIfOrdersExistTest handles empty lists`() {
        val currencyFormat = NumberFormat.getCurrencyInstance()

        val summary = checkIfOrdersExistTest(emptyList(), emptyList(), currencyFormat)

        assertEquals("No Orders", summary.orderId)
        assertEquals("", summary.orderDate)
        assertEquals("", summary.orderAmount)
    }
}
