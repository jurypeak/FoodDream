package com.example.fooddream.controllers

import android.util.Log
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Account
import com.example.fooddream.models.Customer
import com.example.fooddream.models.Product

class CustomerController (
    private var account: Account,
    private var customer: Customer,
    private var controller: AccountController
){

    fun Register(email: String, fName: String, lName: String, password: String): Customer? {
        return try {
            Customer(
                fName,
                lName,
                email,
                account.getAccountId(),
                account.getAccessLevel(),
                password
            )
        } catch (error: Errors.CreationException) {
            Log.d("Account Creation Error", "$error")
            null
        }
    }
    fun ResetPassword(newPassword: String, emailCode: Int): Boolean {
        //TODO add a helper function to verify email codes.
        return try {
            controller.setHashedPassword(newPassword)
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }
    fun ViewAccountDetails(): String {
        return ""
    }
    fun DeleteAccount(): Boolean {
        return try {
            true
        } catch(error: Errors.DeletionException) {
            Log.d("Deletion Error", "$error")
            false
        }
    }
    fun EditEmail(newEmail: String) {
        try {
            account.setEmail(newEmail)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }
    fun EditName(newFName: String, newLName: String) {
        try {
            customer.setFName(newFName)
            customer.setLName(newLName)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }
    fun EditPassword(newPassword: String) {
        try {
            controller.setHashedPassword(newPassword)
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
    }
    fun AddToBasket(product: Product) {
        try {

        } catch (error: Errors.BasketAdditionException) {
            Log.d("Basket Error", "$error")
        }
    }
    fun ViewBasket(): List<Product>? {
        return try {
            listOf<Product>()
        } catch (error: Errors.ViewBasketException) {
            Log.d("Basket Error", "$error")
            null
        }
    }
    fun ViewOrder() {
        // TODO: Implement order retrieval logic
        Log.d("Order", "Fetching order details...")
    }

    fun ViewOrderHistory() {
        // TODO: Implement order history retrieval logic
        Log.d("Order History", "Fetching past orders...")
    }
}