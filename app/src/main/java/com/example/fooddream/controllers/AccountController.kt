package com.example.fooddream.controllers

import CustomerRepository
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.interfaces.IAccountController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer
import com.example.fooddream.utils.AuthenticationManager
import com.example.fooddream.views.VerifyEmailView

class AccountController(
    private var view: AppCompatActivity): IAccountController {

    private val customer = Customer(
        fName = "",
        lName = "",
        email = "",
        accountId = -1,
        accessLevel = -1,
        password = ""
    )
    private var customerRepository = CustomerRepository(view)
    private var notification = Notification()
    private var navigationController = NavigationController(view)
    private var sessionController = SessionController(view)
    private var authenticationManager = AuthenticationManager(
        view,
        customer,
        navigationController,
        sessionController
        )

    override fun sendTwoFactorAuth(
        email: String,
        requestQueue: RequestQueue,
        url: String,
        typeView: String
    ) {
        authenticationManager.sendVerificationEmailCode(
            email,
            requestQueue,
            url,
            typeView
        )
    }

    override fun startLogin(
        loginButton: Button,
        emailField: EditText,
        passwordField: EditText,
    ) {
        try {
            val requestQueue = Volley.newRequestQueue(view)
            var email = emailField.text.toString()
            var password = passwordField.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                Log.d("Login", "$email, $password")
                authenticationManager.login(
                    email,
                    password,
                    requestQueue,
                    BuildConfig.URL_LOGIN
                )
            } else {
                notification.sendNotification("Email or password cannot be empty.", view)
                Log.e("MainActivity", "Email or password cannot be empty.")
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while logging in", view)
            Log.d("Login Handling Error", "$error")
        }
    }

    override fun startRegistration(
        emailField: EditText,
        nameField: EditText,
        passwordField: EditText,
    ) {
        try {
            val email = emailField.text.toString()
            val name = nameField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotBlank() && name.isNotBlank() && password.isNotBlank()) {
                val nameParts = name.split(" ")
                val fName = nameParts.getOrNull(0) ?: ""
                val lName = nameParts.getOrNull(1) ?: ""

                customer.setFName(fName)
                customer.setFName(lName)
                customer.setEmail(email)
                customer.setPassword(password)
                customer.setAccessLevel(1)

                authenticationManager.register(
                    email,
                    fName,
                    lName,
                    password,
                    Volley.newRequestQueue(view),
                    BuildConfig.URL_REGISTER
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    override fun startResetPasswordEmailVerification(
        emailField: EditText,
    ) {
        try {
            val email = emailField.text.toString()
            customer.setEmail(email)
            customerRepository.saveCustomer(customer)

            if (email.isNotBlank()) {
                val bundle = Bundle().apply {
                    putString("email", email)
                    putString("typeView", "Reset Password")
                }
                val verifyEmailFragment = VerifyEmailView()
                verifyEmailFragment.arguments = bundle
                navigationController.replaceActivityWithFragment(
                    verifyEmailFragment,
                    R.id.verify_email_fragment
                )
            }
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    override fun startResetPassword(
        passwordField: EditText,
    ) {
        val password = passwordField.text.toString()
        try {
            authenticationManager.resetPassword(
                customerRepository.getCustomer()?.getEmail().toString(),
                password,
                Volley.newRequestQueue(view),
                BuildConfig.URL_RESETPASSWORD
            )
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while registering", view)
            Log.d("Register Handling Error", "$error")
        }
    }

    override fun viewAccountDetails(): String {
        return ""
    }

    override fun deleteAccount(): Boolean {
        return try {
            true
        } catch (error: Errors.DeletionException) {
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
            authenticationManager.setEncryptedPassword(newPassword)
        } catch (error: Errors.HashingException) {
            Log.d("Hashing Error", "$error")
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