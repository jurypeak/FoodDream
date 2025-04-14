package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.viewControllers.LoginViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification

/**
 * LoginView is an activity that represents the login screen of the application.
 * It handles user interactions and manages the login process.
 *
 * @property loginButton The button used to initiate the login process.
 * @property forgotPasswordButton The button used to navigate to the password recovery screen.
 * @property signUpText The text view that navigates to the sign-up screen.
 * @property emailField The input field for the user's email address.
 * @property userGuideButton The button that opens the user guide.
 * @property passwordField The input field for the user's password.
 * @property customerSupportButton The button that opens customer support.
 *
 * @property notification The notification manager for displaying messages to the user.
 * @property sessionController The controller responsible for managing user sessions.
 * @property accountController The controller responsible for managing user accounts.
 * @property navigationController The controller responsible for managing navigation between views.
 * @property loginViewController The controller responsible for managing the login view logic.
 */
class LoginView : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: TextView
    private lateinit var signUpText: TextView
    private lateinit var emailField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var passwordField: EditText
    private lateinit var customerSupportButton: ImageView

    private lateinit var notification: Notification
    private lateinit var sessionController: SessionController
    private lateinit var accountController: AccountController
    private lateinit var navigationController: NavigationController
    private lateinit var loginViewController: LoginViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        init()
    }

    /**
     * Initialize the login view.
     * This method is responsible for initializing the view components and setting up the UI actions.
     */
    fun init() {
        initializeViewComponents()
        initializeControllers()
        loginViewController.initializeLoginScreen(this)
        setupUIActions()
    }

    /**
     * Initialize the view components for the login screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeControllers() {
        try {
            sessionController = SessionController(this)
            notification = Notification()
            navigationController = NavigationController(this)
            accountController = AccountController(this)

            loginViewController = LoginViewController(
                sessionController,
                navigationController,
                notification,
                accountController
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading login page.", this)
            Log.e("LoginView", "Error initializing controllers: ${e.message}")
        }
    }

    /**
     * Set up the UI actions for the login screen.
     * This method is responsible for setting up the click listeners for the buttons and other UI elements.
     * It also handles any exceptions that may occur during the setup process.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setupUIActions() {
        try {
            loginViewController.setupClickListeners(
                this,
                loginButton,
                emailField,
                passwordField,
                forgotPasswordButton,
                signUpText,
                userGuideButton,
                customerSupportButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading login page.", this)
            Log.e("LoginView", "Error setting up UI actions: ${e.message}")
        }
    }

    /**
     * Initialize the view components for the login screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
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
}