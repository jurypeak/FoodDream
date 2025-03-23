package com.example.fooddream.views

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer

class LoginView : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: TextView
    private lateinit var registerActivity: View
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var customerController: CustomerController
    private lateinit var customer: Customer
    private lateinit var notification: Notification
    private val url = BuildConfig.URL_LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        customerController = CustomerController( this )

        // Initialize the components
        initializeComponents()
        loginUser()
    }

    private fun loginUser(){
        customerController.handleLogin(
            loginButton,
            emailField,
            passwordField,
            url
        )
    }

    private fun initializeComponents() {
        // Initialize elements
        loginButton = findViewById(R.id.login_button)
        forgotPasswordButton = findViewById(R.id.forgotPassPlaceholder)
        registerActivity = findViewById(R.id.register_page)
        emailField = findViewById(R.id.email_login)
        passwordField = findViewById(R.id.password_login)
    }
}