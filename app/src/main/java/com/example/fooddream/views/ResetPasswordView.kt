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
import com.example.fooddream.controllers.viewControllers.ResetPasswordViewController
import com.example.fooddream.messengers.Notification

/**
 * ResetPasswordView is a Fragment that handles the user interface for resetting the password.
 * It allows users to enter their new password and submit it for account recovery.
 *
 * @property exitButton TextView for the exit button.
 * @property passwordField EditText for entering the new password.
 * @property userGuideButton ImageView to access the user guide.
 * @property submitButton Button for submitting the new password.
 * @property customerSupportButton ImageView to access the customer support page.
 *
 * @property customerController Controller for managing customer-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 * @property resetPasswordViewController Controller for managing the reset password view logic.
 */
class ResetPasswordView : Fragment() {
    private lateinit var exitButton: TextView
    private lateinit var passwordField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var submitButton: Button
    private lateinit var customerSupportButton: ImageView

    private lateinit var customerController: CustomerController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification
    private lateinit var resetPasswordViewController: ResetPasswordViewController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reset_password_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the ResetPasswordView by setting up controllers, the UI components and click listeners.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeViewComponents(view)
        initializeControllers()
        setUIActions()
    }

    /**
     * Initializes the controllers used in the ResetPasswordView.
     */
    private fun initializeControllers() {
        customerController = CustomerController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        resetPasswordViewController = ResetPasswordViewController(
            customerController,
            navigationController,
            notification
        )
    }

    /**
     * Sets up the click listeners for the UI components in the ResetPasswordView.
     * This includes the exit button, user guide button, submit button, and customer support button.
     */
    private fun setUIActions() {
        try {
            resetPasswordViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                exitButton,
                passwordField,
                userGuideButton,
                submitButton,
                customerSupportButton
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading reset password page.", requireActivity() as AppCompatActivity)
            Log.e("ResetPasswordView", "Error setting up UI actions: ${e.message}")
        }
    }

    /**
     * Initializes the view components for the ResetPasswordView.
     * This method is responsible for finding and assigning the views to their respective variables.
     *
     * @param view The root view of the fragment.
     */
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
}