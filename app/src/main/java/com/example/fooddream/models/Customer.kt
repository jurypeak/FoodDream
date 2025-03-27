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
    fun setFName(newName: String){
        fName = newName
    }
    fun setLName(newName: String){
        lName = newName
    }
}
