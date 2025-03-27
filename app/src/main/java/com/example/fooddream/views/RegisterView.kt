package com.example.fooddream.views

import android.os.Bundle
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
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController

class RegisterView : Fragment() {
    private lateinit var registerButton: Button
    private lateinit var loginButton: TextView
    private lateinit var forgotPasswordButton: TextView
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var nameField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView
    private lateinit var customerController: CustomerController
    private lateinit var navigationController: NavigationController
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerController = CustomerController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)

        initializeViewComponents(view)
        setUpListeners()

        customerController.handleRegistration(
            registerButton,
            emailField,
            nameField,
            passwordField,
        )
    }

    private fun initializeViewComponents(view: View) {
        registerButton = view.findViewById(R.id.register_button)
        loginButton = view.findViewById(R.id.loginPlaceholder)
        userGuideButton = view.findViewById(R.id.helpIcon)
        forgotPasswordButton = view.findViewById(R.id.forgotPassPlaceholder)
        emailField = view.findViewById(R.id.email_register)
        nameField = view.findViewById(R.id.nameRegister)
        passwordField = view.findViewById(R.id.password_register)
        customerSupportButton = view.findViewById(R.id.customerSupportIcon)
    }

    private fun setUpListeners() {
        loginButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        userGuideButton.setOnClickListener {
            navigationController.navigateToUserGuide(urlUserGuide)
        }
        customerSupportButton.setOnClickListener {
            navigationController.navigateToFragment(
                CustomerSupportView(),
                R.id.customer_support_fragment
            )
        }
    }
}