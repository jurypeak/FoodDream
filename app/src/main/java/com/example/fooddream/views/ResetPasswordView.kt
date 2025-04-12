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
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification

class ResetPasswordView : Fragment() {
    private lateinit var exitButton: TextView
    private lateinit var passwordField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var submitButton: Button
    private lateinit var customerSupportButton: ImageView
    private lateinit var customerController: CustomerController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification
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
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setUpListeners()
    }

    private fun initializeViewComponents(view: View) {
        try {
            exitButton = view.findViewById(R.id.exitPlaceholder)
            userGuideButton = view.findViewById(R.id.helpIcon)
            submitButton = view.findViewById(R.id.submit_button)
            customerSupportButton = view.findViewById(R.id.customerSupportIcon)

            passwordField = view.findViewById(R.id.new_password)
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordView", "Error initializing components: ${e.message}")
        }
    }

    private fun setUpListeners() {
        try {
            exitButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            userGuideButton.setOnClickListener {
                navigationController.navigateToUserGuide(urlUserGuide)
            }
            customerSupportButton.setOnClickListener {
                navigationController.navigateToFragment(
                    CustomerSupportView(),
                    R.id.fragment_container
                )
            }
            submitButton.setOnClickListener {
                customerController.handleResetPassword(passwordField)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordView", "Error setting up listeners: ${e.message}")
        }
    }
}