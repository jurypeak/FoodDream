package com.example.fooddream.models

class Customer(
    private var fName: String,
    private var lName: String,
    email: String,
    accountId: Int,
    accessLevel: Int,
    password: String
) : Account(
    email,
    accountId,
    accessLevel,
    password
) {
    //Getters
    fun getFName(): String = fName
    fun getLName(): String = lName

    //Setters
    fun setFName(newFName: String) {
        fName = newFName
    }
    fun setLName(newLName: String) {
        lName = newLName
    }
}
