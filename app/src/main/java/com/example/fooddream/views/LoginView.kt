package com.example.fooddream.views

import CustomerRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product
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
    private lateinit var notification: Notification
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        val sessionManager = SessionManager(this)
        val notification = Notification()

        try {
            if (sessionManager.hasSession()) {
                Log.d("LoginView", "User already logged in, redirecting...")
                startActivity(Intent(this, CustomerCatalogView::class.java))
                finish()
                return
            }
        } catch (e: Exception) {
            notification.sendNotification("Error checking user session.", this)
            Log.e("LoginView", "Error checking session: ${e.message}")
        }

        customerController = CustomerController( this )
        viewController = NavigationController( this )

        init()
    }

    private fun init() {
        initializeViewComponents()
        setUpListeners()
    }

    private fun initializeViewComponents() {
        try {
            loginButton = findViewById(R.id.login_button)
            forgotPasswordButton = findViewById(R.id.forgotPassPlaceholder)
            userGuideButton = findViewById(R.id.helpIcon)
            customerSupportButton = findViewById(R.id.customerSupportIcon)

            emailField = findViewById(R.id.email_login)
            passwordField = findViewById(R.id.password_login)

            signUpText = findViewById(R.id.signUpPlaceholder)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading login page.", this)
            Log.e("LoginView", "Error initializing components: ${e.message}")
        }
    }

    private fun setUpListeners() {
        try {
            signUpText.setOnClickListener {
                viewController.navigateToFragment(
                    RegisterView(),
                    R.id.fragment_container
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
                    R.id.fragment_container
                )
            }
            forgotPasswordButton.setOnClickListener {
                viewController.navigateToFragment(
                    ResetPasswordEmailView(),
                    R.id.fragment_container
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading login page.", this)
            Log.e("LoginView", "Error setting up listeners: ${e.message}")
        }
    }
}