package com.example.fooddream.models

class BasketItem (
    private var basketItemId: Int,
    private var productId: Int,
    private var basketId: Int,
    private var quantity: Int,
    private var price: Double,
    private var itemName: String,
    private var basket: Basket
) {
    //Getters
    fun getBasketItemId(): Int = basketItemId
    fun getProductId(): Int = productId
    fun getBasketId(): Int = basketId
    fun getQuantity(): Int = quantity
    fun getPrice(): Double = price
    fun getItemName(): String = itemName

    //Setters
    fun setBasketItemId(newId: Int) {
        basketItemId = newId
    }
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