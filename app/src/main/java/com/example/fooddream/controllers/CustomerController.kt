package com.example.fooddream.controllers

import android.util.Log
import com.example.fooddream.interfaces.IAccountController
import com.example.fooddream.interfaces.IBasketController
import com.example.fooddream.interfaces.ICustomerController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.models.Customer
import com.example.fooddream.models.Product
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.messengers.Notification
import com.example.fooddream.views.MainActivity
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt

class CustomerController (
    private var customer: Customer,
    private var notification: Notification,
    private var view: MainActivity
) : IAccountController,
    IBasketController,
    ICustomerController {

    override fun register(email: String, fName: String, lName: String, password: String): Customer? {
        return try {
            Customer(
                fName,
                lName,
                email,
                customer.getAccountId(),
                customer.getAccessLevel(),
                password
            )
        } catch (error: Errors.CreationException) {
            Log.d("Account Creation Error", "$error")
            null
        }
    }
    override fun resetPassword(newPassword: String, emailCode: Int): Boolean {
        //TODO add a helper function to verify email codes.
        return try {
            setHashedPassword(newPassword)
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }
    override fun viewAccountDetails(): String {
        return ""
    }
    override fun deleteAccount(): Boolean {
        return try {
            true
        } catch(error: Errors.DeletionException) {
            Log.d("Deletion Error", "$error")
            false
        }
    }
    override fun editEmail(newEmail: String) {
        try {
            customer.setEmail(newEmail)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }
    override fun editName(newFName: String, newLName: String) {
        try {
            customer.setFName(newFName)
            customer.setLName(newLName)
        } catch (error: Errors.SetException) {
            Log.d("Set Error", "$error")
        }
    }
    override fun editPassword(newPassword: String) {
        try {
            setHashedPassword(newPassword)
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
    }
    override fun addToBasket(product: Product) {
        try {

        } catch (error: Errors.BasketAdditionException) {
            Log.d("Basket Error", "$error")
        }
    }
    override fun viewBasket(): List<Product>? {
        return try {
            listOf<Product>()
        } catch (error: Errors.ViewBasketException) {
            Log.d("Basket Error", "$error")
            null
        }
    }
    override fun removeItem(id: Int) {
        TODO("Not yet implemented")
    }
    override fun editQuantity(id: Int, quantity: Int) {
        TODO("Not yet implemented")
    }
    override fun validateStock(): Boolean {
        TODO("Not yet implemented")
    }
    override fun toCheckout(): Boolean {
        TODO("Not yet implemented")
    }
    override fun viewOrder() {
        // TODO: Implement order retrieval logic
        Log.d("Order", "Fetching order details...")
    }
    override fun viewOrderHistory() {
        // TODO: Implement order history retrieval logic
        Log.d("Order History", "Fetching past orders...")
    }
    // Function for encrypting password with BCrypt algorithm
    private fun hashPassword(password:String): String?{
        try {
            val salt = BCrypt.gensalt(12)
            var password = BCrypt.hashpw(password, salt)
            return password
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
        }
        return null
    }
    // Function to check inputted password against stored hashed password.
    private fun verifyPassword(
        password:String,
        hashedPassword:String
    ): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (error: Errors.ComparingException) {
            Log.d("Password Comparing Error", "$error")
            false
        }
    }
    // Function to hash passwords without showing the algorithm
    override fun setHashedPassword(password: String): Boolean {
        return try {
            hashPassword(password).toString()
            true
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
            false
        }
    }
    // Function to allow users to login into their account.
    override fun login(
        email:String,
        password:String,
        requestQueue:RequestQueue,
        url:String
    ) {
        try {
        val jsonObject = JSONObject().apply {
            put("email", email)
            Log.d("Email", email)
            var password = hashPassword(password)
            put("password", password)
            Log.d("password", password.toString())
        }
            val jsonObjectRequest = JsonObjectRequest(
               Request.Method.POST, url, jsonObject,
               { response ->
                   val returnedPassword = response.optString("password", "")
                   if(verifyPassword(password, returnedPassword)) {
                       Log.d("Response", "$response")
                       notification.sendNotification("Welcome ${response.optString("CustomerFName", "")}", view)
                   } else {
                       notification.sendNotification("Password do not match", view)
                   }
               },
               { error ->
                   notification.sendNotification("$error.toString", view)
                   Log.d("Error", "$error")
               })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Errors.LoginException) {
            Log.d("Login Error", "$error")
        }
    }

    // Function that closes users sessions and logs users out.
    override fun logout(
        sessionId: Int
    ): Boolean {
        //TODO Logout needs sessions to be implemented.
        return false
    }
}