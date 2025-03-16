package com.example.fooddream.models

class Basket (
    private var items: MutableList<BasketItem> = mutableListOf(),
    private var totalPrice: Double,
    private var accountId: Int,
    private var guestId: Int,
    private var basketId: Int,
    private var totalItems: Int
) {
    //Getters
    fun getItems(): List<BasketItem> = items
    fun getTotalPrice(): Double = totalPrice
    fun getAccountId(): Int = accountId
    fun getGuestId(): Int = guestId
    fun basketId(): Int = basketId
    fun totalItems(): Int = totalItems

    //Setters
    fun addItems(newItems: BasketItem) {
        items.add(newItems)
    }
    fun setTotalPrice(newTotalPrice: Double) {
        totalPrice = newTotalPrice
    }
    fun setAccountId(newAccountId: Int) {
        accountId = newAccountId
    }
    fun setGuestId(newGuestId: Int) {
        guestId = newGuestId
    }
    fun setBasketId(newBasketId: Int) {
        basketId = newBasketId
    }
    fun setTotalItems(newTotalItems: Int) {
        totalItems = newTotalItems
    }
}