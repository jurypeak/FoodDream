package com.example.fooddream.controllers

import com.example.fooddream.models.Basket
import com.example.fooddream.models.Checkout

class CheckoutController (
    private var checkout: Checkout,
    private var basket: Basket
) {

    fun isBasketEmpty(): Boolean {
        return true
    }
    fun editQuantity(id: Int) {

    }
    fun validateStock() {

    }
    fun payment(paymentMethod: String) {

    }
    fun orderSummary(
        name: String,
        email: String,
        street: String,
        postcode: String,
        town: String,
        paymentMethod: String,
        amount: Double,
        orderId: Int,
        totalItems: Int
    ) {

    }
}