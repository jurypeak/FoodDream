package com.example.fooddream.models

import java.util.Date

class Payment(
    private var paymentId: Int,
    private var orderId: Int,
    private var paymentDate: Date,
    private var paymentMethod: String,
    private var amount: Double
) {
    // Getters
    fun getPaymentId(): Int = paymentId
    fun getOrderId(): Int = orderId
    fun getPaymentDate(): Date = paymentDate
    fun getPaymentMethod(): String = paymentMethod
    fun getAmount(): Double = amount

    // Setters
    fun setPaymentId(newId: Int) {
        paymentId = newId
    }

    fun setOrderId(newId: Int) {
        orderId = newId
    }

    fun setPaymentDate(newDate: Date) {
        paymentDate = newDate
    }

    fun setPaymentMethod(newMethod: String) {
        paymentMethod = newMethod
    }

    fun setAmount(newAmount: Double) {
        amount = newAmount
    }
}
