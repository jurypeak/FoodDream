package com.example.fooddream.models

class Payment(
    private var paymentId: Int,
    private var orderId: Int,
    private var paymentMethod: String,
    private var paymentDate: String,
    private var amount: Double
) {
    // Getters
    fun getPaymentId(): Int = paymentId
    fun getOrderId(): Int = orderId
    fun getPaymentMethod(): String = paymentMethod
    fun getAmount(): Double = amount
    fun getPaymentDate(): String = paymentDate

    // Setters
    fun setPaymentId(newId: Int) {
        paymentId = newId
    }
    fun setOrderId(newId: Int) {
        orderId = newId
    }

    fun setPaymentMethod(newMethod: String) {
        paymentMethod = newMethod
    }
    fun setAmount(newAmount: Double) {
        amount = newAmount
    }
    fun setPaymentDate(newDate: String) {
        paymentDate = newDate
    }
}
