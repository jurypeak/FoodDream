package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.RegisterViewController
import com.example.fooddream.messengers.Notification

class RegisterView : Fragment() {
    private lateinit var registerButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var forgotPasswordButton: TextView
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var nameField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView

    private lateinit var navigationController: NavigationController
    private lateinit var registerViewController: RegisterViewController
    private lateinit var accountController: AccountController
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initialize the registration view.
     * This method is responsible for initializing the view components and setting up the UI actions.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeViewComponents(view)
        initializeControllers()
        setupUIActions()
    }

    /**
     * Initialize the view components for the registration screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeControllers() {
        try {
            accountController = AccountController(requireActivity() as AppCompatActivity)
            navigationController = NavigationController(requireActivity() as AppCompatActivity)
            notification = Notification()

            registerViewController = RegisterViewController(
                navigationController,
                notification,
                accountController
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred loading registration.", requireActivity() as AppCompatActivity)
            Log.e("Registration Error", "Error loading registration: ${e.message}")
        }
    }

    /**
     * Set up the UI actions for the registration screen.
     * This method is responsible for setting up the click listeners for the buttons and other UI elements.
     * It also handles any exceptions that may occur during the setup process.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setupUIActions() {
        try {
            registerViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                registerButton,
                emailField,
                nameField,
                passwordField,
                forgotPasswordButton,
                loginTextView,
                userGuideButton,
                customerSupportButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred setting up registration", requireActivity() as AppCompatActivity)
            Log.e("Registration Error", "Error setting up UI actions: ${e.message}")
        }
    }

    /**
     * Initialize the view components for the registration screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @param view The root view of the fragment.
     */
    private fun initializeViewComponents(view: View) {
        try {
            registerButton = view.findViewById(R.id.register_button)
            loginTextView = view.findViewById(R.id.loginPlaceholder)
            userGuideButton = view.findViewById(R.id.helpIcon)
            forgotPasswordButton = view.findViewById(R.id.forgotPassPlaceholder)
            customerSupportButton = view.findViewById(R.id.customerSupportIcon)

            emailField = view.findViewById(R.id.email_register)
            nameField = view.findViewById(R.id.nameRegister)
            passwordField = view.findViewById(R.id.password_register)
        } catch (e: Exception) {
            notification.sendNotification("Error occurred loading registration.", requireActivity() as AppCompatActivity)
            Log.e("Registration Error", "Error loading registration: ${e.message}")
        }
    }
}