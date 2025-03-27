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

class ResetPasswordView : Fragment() {
    private lateinit var exitButton: TextView
    private lateinit var passwordField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var submitButton: Button
    private lateinit var customerSupportButton: ImageView
    private lateinit var customerController: CustomerController
    private lateinit var navigationController: NavigationController
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reset_password_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerController = CustomerController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)

        initializeViewComponents(view)
        setUpListeners()

    }

    private fun initializeViewComponents(view: View) {
        exitButton = view.findViewById(R.id.exitPlaceholder)
        userGuideButton = view.findViewById(R.id.helpIcon)
        submitButton = view.findViewById(R.id.submit_button)
        passwordField = view.findViewById(R.id.new_password)
        customerSupportButton = view.findViewById(R.id.customerSupportIcon)
    }

    private fun setUpListeners() {
        exitButton.setOnClickListener {
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
        submitButton.setOnClickListener {
            customerController.handleResetPassword(passwordField)
        }
    }
}