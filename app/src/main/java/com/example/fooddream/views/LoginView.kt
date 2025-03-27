package com.example.fooddream.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.utils.SessionManager

class LoginView : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: TextView
    private lateinit var signUpText: TextView
    private lateinit var emailField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var passwordField: EditText
    private lateinit var customerSupportButton: ImageView
    private lateinit var customerController: CustomerController
    private lateinit var viewController: NavigationController
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)


        val sessionManager = SessionManager(this)
        if (sessionManager.hasSession()) {
            Log.d("LoginView", "User already logged in, redirecting...")
            startActivity(Intent(this, CustomerCatalogView::class.java))
            finish()
            return
        }

        customerController = CustomerController( this )
        viewController = NavigationController( this )


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
            viewController.navigateToFragment(
                RegisterView(),
                R.id.register_fragment
            )
        }
        loginButton.setOnClickListener {
            customerController.handleLogin(
                loginButton,
                emailField,
                passwordField,
            )
        }
        userGuideButton.setOnClickListener {
            viewController.navigateToUserGuide(urlUserGuide)
        }
        customerSupportButton.setOnClickListener {
            viewController.navigateToFragment(
                CustomerSupportView(),
                R.id.customer_support_fragment
            )
        }
    }
}