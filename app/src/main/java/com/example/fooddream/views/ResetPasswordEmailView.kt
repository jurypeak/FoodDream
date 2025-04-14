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
import com.example.fooddream.controllers.CustomerController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.ResetPasswordEmailViewController
import com.example.fooddream.messengers.Notification

/**
 * ResetPasswordEmailView is a Fragment that handles the user interface for resetting the password
 * via email. It allows users to enter their email address and submit it for password reset.
 *
 * @property exitTextView TextView for the exit button.
 * @property emailField EditText for entering the email address.
 * @property userGuideButton ImageView to access the user guide.
 * @property submitButton Button for submitting the email address.
 * @property customerSupportButton ImageView to access the customer support page.
 *
 * @property customerController Controller for managing customer-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property resetPasswordEmailViewController Controller for managing the reset password email view logic.
 * @property notification Notification manager for displaying messages to the user.
 */
class ResetPasswordEmailView : Fragment() {
    private lateinit var exitTextView: TextView
    private lateinit var emailField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var submitButton: Button
    private lateinit var customerSupportButton: ImageView

    private lateinit var customerController: CustomerController
    private lateinit var navigationController: NavigationController
    private lateinit var resetPasswordEmailViewController: ResetPasswordEmailViewController
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reset_password_email_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the view components, controllers and sets up the click listeners for the buttons.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeViewComponents(view)
        initializeControllers()
        setUIActions()
    }

    /**
     * Initializes the controllers used in the ResetPasswordEmailView.
     * This includes setting up the CustomerController, NavigationController, and Notification.
     */
    private fun initializeControllers() {
        try {
            customerController = CustomerController(requireActivity() as AppCompatActivity)
            navigationController = NavigationController(requireActivity() as AppCompatActivity)
            notification = Notification()

            resetPasswordEmailViewController = ResetPasswordEmailViewController(
                navigationController,
                notification,
                customerController
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordEmailView", "Error loading reset password page: ${e.message}")
        }
    }

    /**
     * Sets up the click listeners for the UI components in the ResetPasswordEmailView.
     * This includes setting up listeners for the exit button, user guide button, submit button,
     * and customer support button.
     */
    private fun setUIActions() {
        try {
            resetPasswordEmailViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                exitTextView,
                emailField,
                userGuideButton,
                submitButton,
                customerSupportButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordEmailView", "Error setting up listeners: ${e.message}")
        }
    }

    /**
     * Initializes the view components for the ResetPasswordEmailView.
     * This includes finding and assigning the views to their respective variables.
     *
     * @param view The root view of the fragment.
     */
    private fun initializeViewComponents(view: View) {
        try {
            exitTextView = view.findViewById(R.id.exitPlaceholder)
            userGuideButton = view.findViewById(R.id.helpIcon)
            submitButton = view.findViewById(R.id.submit_button)
            customerSupportButton = view.findViewById(R.id.customerSupportIcon)

            emailField = view.findViewById(R.id.enter_email)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordEmailView", "Error initializing components: ${e.message}")
        }
    }
}