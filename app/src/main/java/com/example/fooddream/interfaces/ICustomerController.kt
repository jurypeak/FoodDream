package com.example.fooddream.interfaces

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.android.volley.RequestQueue
import com.example.fooddream.models.Customer
import com.example.fooddream.models.Product

interface ICustomerController {

    fun register(email: String, fName: String, lName: String, password: String, requestQueue: RequestQueue, url: String)
    fun resetPassword(newPassword: String, emailCode: Int): Boolean
    fun deleteAccount(): Boolean
    fun viewAccountDetails(): String
    fun editEmail(newEmail: String)
    fun editName(newFName: String, newLName: String)
    fun editPassword(newPassword: String)
    fun verifyUserEmailCode(submitButton: Button, codeField: EditText, verificationCode: String)
    fun sendVerificationEmailCode(email: String, requestQueue: RequestQueue, url: String)

    fun addToBasket(product: Product)
    fun viewBasket(): List<Product>?
    fun removeItem(id: Int)
    fun editQuantity(id: Int, quantity: Int)
    fun validateStock(): Boolean
    fun toCheckout(): Boolean

    fun viewOrder()
    fun viewOrderHistory()

    fun setEncryptedPassword(password: String): Boolean
    fun login(
        email: String,
        password: String,
        requestQueue: RequestQueue,
        url: String
    )
    fun startLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
        url: String
    )
    fun startRegistration(
        registerButton: Button,
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    )
    fun logout(sessionId: Int): Boolean
}
