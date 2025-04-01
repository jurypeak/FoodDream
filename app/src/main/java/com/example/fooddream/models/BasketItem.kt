package com.example.fooddream.models

class BasketItem (
    private var productId: Int,
    private var basketId: Int,
    private var quantity: Int,
    private var price: Double,
    private var itemName: String,
) {
    //Getters
    fun getProductId(): Int = productId
    fun getBasketId(): Int = basketId
    fun getQuantity(): Int = quantity
    fun getPrice(): Double = price
    fun getItemName(): String = itemName

    //Setters
    fun setProductId(newId: Int) {
        productId = newId
    }
    fun setBasketId(newId: Int) {
        basketId = newId
    }
    fun setQuantity(newQuantity: Int) {
        quantity = newQuantity
    }
    fun setPrice(newPrice: Double) {
        price = newPrice
    }
    fun setItemName(newItemName: String) {
        itemName = newItemName
    }
}