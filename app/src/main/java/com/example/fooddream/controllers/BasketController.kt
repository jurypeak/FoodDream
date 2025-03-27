package com.example.fooddream.controllers

import android.util.Log
import com.example.fooddream.interfaces.IBasketController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Product

class BasketController: IBasketController {
    override fun addToBasket(product: Product) {
        try {

        } catch (error: Errors.BasketAdditionException) {
            Log.d("Basket Error", "$error")
        }
    }
    override fun viewBasket(): List<Product>? {
        return try {
            listOf<Product>()
        } catch (error: Errors.ViewBasketException) {
            Log.d("Basket Error", "$error")
            null
        }
    }
    override fun removeItem(id: Int) {
        TODO("Not yet implemented")
    }
    override fun editQuantity(id: Int, quantity: Int) {
        TODO("Not yet implemented")
    }
    override fun validateStock(): Boolean {
        TODO("Not yet implemented")
    }
    override fun toCheckout(): Boolean {
        TODO("Not yet implemented")
    }
}