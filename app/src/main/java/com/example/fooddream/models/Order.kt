package com.example.fooddream.models

class Order(
    private var fName: String,
    private var lName: String,
    private var email: String,
    private var accountId: Int?,
    private var orderId: Int,
    private var orderDate: String,
) {
    // Getters
    fun getFName(): String = fName
    fun getLName(): String = lName
    fun getEmail(): String = email
    fun getAccountId(): Int? = accountId
    fun getOrderId(): Int = orderId
    fun getOrderDate(): String = orderDate

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
    fun setOrderId(newOrderId: Int) {
        orderId = newOrderId
    }
    fun setOrderDate(newOrderDate: String) {
        orderDate = newOrderDate
    }
}

