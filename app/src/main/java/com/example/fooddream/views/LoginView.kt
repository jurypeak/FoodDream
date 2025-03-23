package com.example.fooddream.views

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    private var url = BuildConfig.URL_LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        customerController = CustomerController( this )

        initializeViewComponents()
        handleUserLogin()
    }

    private fun handleUserLogin(){
        customerController.handleLogin(
            loginButton,
            emailField,
            passwordField,
            url
        )
    }

    private fun initializeViewComponents() {
        loginButton = findViewById(R.id.login_button)
        forgotPasswordButton = findViewById(R.id.forgotPassPlaceholder)
        registerActivity = findViewById(R.id.register_page)
        emailField = findViewById(R.id.email_login)
        passwordField = findViewById(R.id.password_login)
    }

}