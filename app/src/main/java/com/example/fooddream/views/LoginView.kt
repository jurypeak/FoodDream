package com.example.fooddream.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController

class LoginView : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: TextView
    private lateinit var signUpText: TextView
    private lateinit var emailField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var passwordField: EditText
    private lateinit var customerSupportButton: ImageView
    private lateinit var controller: CustomerController
    private var urlLogin = BuildConfig.URL_LOGIN
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        controller = CustomerController( this )

        initializeViewComponents()
        setUpListeners()
    }

    private fun initializeViewComponents() {
        loginButton = findViewById(R.id.login_button)
        forgotPasswordButton = findViewById(R.id.forgotPassPlaceholder)
        signUpText = findViewById(R.id.signUpPlaceholder)
        userGuideButton = findViewById(R.id.helpIcon)
        emailField = findViewById(R.id.email_login)
        passwordField = findViewById(R.id.password_login)
        customerSupportButton = findViewById(R.id.customerSupportIcon)
    }

    private fun setUpListeners() {
        signUpText.setOnClickListener {
            controller.createRegisterView()
        }
        loginButton.setOnClickListener {
            controller.startLogin(
                loginButton,
                emailField,
                passwordField,
                urlLogin
            )
        }
        userGuideButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, urlUserGuide.toUri())
            intent.setPackage("com.android.chrome")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (error: ActivityNotFoundException) {
                Log.e("Chrome Error", "$error")
                intent.setPackage(null)
                startActivity(intent)
            }
        }
        customerSupportButton.setOnClickListener {
            controller.createCustomerSupportView()
        }
    }
}