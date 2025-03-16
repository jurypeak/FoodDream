package com.example.fooddream.models

import java.util.Date

class Order(
    private var checkout: Checkout,
    private var name: String,
    private var email: String,
    private var accountId: Int?,
    private var guestId: Int?,
    private var date: Date,
    private var orderId: Int,
    private var products: MutableList<OrderItem> = mutableListOf()
) {
    // Getters
    fun getCheckout(): Checkout = checkout
    fun getName(): String = name
    fun getEmail(): String = email
    fun getAccountId(): Int? = accountId
    fun getGuestId(): Int? = guestId
    fun getDate(): Date = date
    fun getOrderId(): Int = orderId
    fun getProducts(): MutableList<OrderItem> = products

    // Setters
    fun setCheckout(newCheckout: Checkout) {
        checkout = newCheckout
    }
    fun setName(newName: String) {
        name = newName
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
    fun setDate(newDate: Date) {
        date = newDate
    }
    fun setOrderId(newOrderId: Int) {
        orderId = newOrderId
    }
    fun setProducts(newProducts: MutableList<OrderItem>) {
        products = newProducts
    }
}

