package com.example.fooddream.interfaces
import com.example.fooddream.models.Product

interface IBasketController {
    fun addToBasket(product: Product)
    fun viewBasket(): List<Product>?
    fun removeItem(id: Int)
    fun editQuantity(id: Int, quantity: Int)
    fun validateStock(): Boolean
    fun toCheckout(): Boolean
}