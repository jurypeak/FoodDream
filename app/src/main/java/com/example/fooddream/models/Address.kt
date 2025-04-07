package com.example.fooddream.models

class Address(
    private var addressId: Int,
    private var orderId: Int,
    private var street: String,
    private var postcode: String,
    private var town: String
) {
    // Getters
    fun getAddressId(): Int = addressId
    fun getOrderId(): Int = orderId
    fun getStreet(): String = street
    fun getPostcode(): String = postcode
    fun getTown(): String = town

    // Setters
    fun setAddressId(newId: Int) {
        addressId = newId
    }

    fun setOrderId(newId: Int) {
        orderId = newId
    }
    fun setStreet(newStreet: String) {
        street = newStreet
    }
    fun setPostcode(newPostcode: String) {
        postcode = newPostcode
    }
    fun setTown(newTown: String) {
        town = newTown
    }
}
