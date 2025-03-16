package com.example.fooddream.models

class Guests (
    private var guestId: Int
) {
    //Getters
    fun getGuestId(): Int = guestId

    //Setters
    fun setGuestId(newGuestId: Int) {
        guestId = newGuestId
    }
}