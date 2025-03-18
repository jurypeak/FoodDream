package com.example.fooddream.views

import com.example.fooddream.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.fooddream.BuildConfig
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Customer

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: TextView
    private lateinit var registerActivity: View
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var customerController: CustomerController
    private lateinit var requestQueue: RequestQueue
    private lateinit var customer: Customer
    private lateinit var notification: Notification
    private val url = BuildConfig.URL_LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        // Initialize the components
        initializeComponents()
    }

    private fun initializeComponents() {
        // Initialize views
        loginButton = findViewById(R.id.login_button)
        forgotPasswordButton = findViewById(R.id.forgotPassPlaceholder)
        registerActivity = findViewById(R.id.register_page)
        emailField = findViewById(R.id.email_login)
        passwordField = findViewById(R.id.password_login)

        notification = Notification()
        customer = Customer(
            fName = "v",
            lName = "sad",
            email = "fsvs",
            accountId = 1,
            accessLevel = 1,
            password = "dxvxv"
        )
        // Initialize the customerController and requestQueue
        customerController = CustomerController(customer, notification, this) // Make sure to initialize your controller properly
        requestQueue = Volley.newRequestQueue(this)

                // Set up the login button click listener to call the login function
            loginButton.setOnClickListener {
                val email = emailField.text.toString()
                val password = passwordField.text.toString()

                if (email.isNotBlank() && password.isNotBlank()) {
                    customerController.login(email, password, requestQueue, url)
                } else {
                    // Handle invalid email or password
                    Log.e("MainActivity", "Email or password cannot be empty")
                }
            }
    }
}
