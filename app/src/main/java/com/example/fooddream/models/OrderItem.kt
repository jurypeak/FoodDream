package com.example.fooddream.models

class OrderItem(
    private var orderItemId: Int,
    private var productId: Int,
    private var orderId: Int,
    private var quantity: Int,
    private var price: Double,
    private var itemName: String
) {
    // Getters
    fun getOrderItemId(): Int = orderItemId
    fun getProductId(): Int = productId
    fun getOrderId(): Int = orderId
    fun getQuantity(): Int = quantity
    fun getPrice(): Double = price
    fun getItemName(): String = itemName

    // Setters
    fun setOrderItemId(newId: Int) {
        orderItemId = newId
    }
    fun setProductId(newId: Int) {
        productId = newId
    }
    fun setOrderId(newId: Int) {
        orderId = newId
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
