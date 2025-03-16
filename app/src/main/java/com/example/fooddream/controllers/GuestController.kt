package com.example.fooddream.controllers

import android.util.Log
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Product

class GuestController {
    fun AddToBasket(product: Product) {
        try {

        } catch (error: Errors.BasketAdditionException) {
            Log.d("Basket Error", "$error")
        }
    }
    fun ViewBasket(): List<Product>? {
        return try {
            listOf<Product>()
        } catch (error: Errors.ViewBasketException) {
            Log.d("Basket Error", "$error")
            null
        }
    }
}