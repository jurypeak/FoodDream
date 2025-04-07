package com.example.fooddream.models

import java.util.Date

class Order(
    private var fName: String,
    private var lName: String,
    private var email: String,
    private var accountId: Int?,
    private var guestId: Int?,
    private var orderId: Int,
    private var products: MutableList<OrderItem> = mutableListOf()
) {
    // Getters
    fun getFName(): String = fName
    fun getLName(): String = lName
    fun getEmail(): String = email
    fun getAccountId(): Int? = accountId
    fun getGuestId(): Int? = guestId
    fun getOrderId(): Int = orderId
    fun getProducts(): MutableList<OrderItem> = products

    // Setters
    fun setFName(newName: String) {
        fName = newName
    }
    fun setLName(newName: String) {
        lName = newName
    }
    fun setEmail(newEmail: String) {
        email = newEmail
    }
    fun setAccountId(newAccountId: Int?) {
        accountId = newAccountId
    }
    fun setGuestId(newGuestId: Int?) {
        guestId = newGuestId
    }
    fun setOrderId(newOrderId: Int) {
        orderId = newOrderId
    }
    fun setProducts(newProducts: MutableList<OrderItem>) {
        products = newProducts
    }
}

