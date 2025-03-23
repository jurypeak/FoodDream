package com.example.fooddream.models

class Checkout (
    private var basketId: Int,
    private var email: String,
    private var fName: String,
    private var lName: String,
    private var address: String,
    private var paymentMethod: String,
    private var totalPrice: Double
) {
    // Getters
    fun getBasketId(): Int = basketId
    fun getEmail(): String = email
    fun getFName(): String = fName
    fun getLName(): String = lName
    fun getAddress(): String = address
    fun getPaymentMethod(): String = paymentMethod
    fun getTotalPrice(): Double = totalPrice

    // Setters
    fun setBasketId(newBasketId: Int) {
        basketId = newBasketId
    }
    fun setEmail(newEmail: String) {
        email = newEmail
    }
    fun setFName(newFName: String) {
        fName = newFName
    }
    fun setLName(newLName: String) {
        lName = newLName
    }
    fun setAddress(newAddress: String) {
        address = newAddress
    }
    fun setPaymentMethod(newPaymentMethod: String) {
        paymentMethod = newPaymentMethod
    }
    fun setTotalPrice(newTotalPrice: Double) {
        totalPrice = newTotalPrice
    }
}
