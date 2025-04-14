package com.example.fooddream.views

import androidx.fragment.app.Fragment
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
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.VerifyEmailViewController
import com.example.fooddream.messengers.Notification

/**
 * VerifyEmailView is a Fragment that handles the user interface for verifying the user's email address.
 * It allows users to enter a verification code sent to their email and submit it for verification.
 *
 * @property submitButton Button for submitting the verification code.
 * @property exitButton TextView for the exit button.
 * @property emailCodeField EditText for entering the verification code.
 * @property userGuideButton ImageView to access the user guide.
 * @property customerSupportButton ImageView to access the customer support page.
 *
 * @property verifyEmailViewController Controller for managing the verify email view logic.
 * @property accountController Controller for managing account-related actions.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class VerifyEmailView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailCodeField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView

    private lateinit var verifyEmailViewController: VerifyEmailViewController
    private lateinit var accountController: AccountController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.verify_email_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        accountController = AccountController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    /**
     * Initializes the VerifyEmailView by setting up the view components UI actions and controllers.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs during initialization.
     */
    private fun init(view: View) {
        try {
            var email = arguments?.getString("email") ?: ""
            var typeView = arguments?.getString("typeView") ?: ""

            initializeViewComponents(view)
            initializeControllers()
            verifyEmailViewController.initializeVerifyEmailScreen(
                requireActivity() as AppCompatActivity,
                email,
                typeView
            )
            setUIActions()
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error initializing VerifyEmailView", e)
        }
    }

    /**
     * Initializes the controllers for the VerifyEmailView.
     * This includes setting up the AccountController, VerifyEmailViewController, NavigationController, and Notification.
     */
    fun initializeControllers() {
        accountController = AccountController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        verifyEmailViewController = VerifyEmailViewController(
            accountController,
            navigationController,
            notification
        )
    }

    /**
     * Sets up the click listeners for the UI components in the VerifyEmailView.
     * This includes the exit button, user guide button, submit button, and customer support button.
     */
    fun setUIActions() {
        try {
            verifyEmailViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                exitButton,
                userGuideButton,
                customerSupportButton,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error setting up UI actions", e)
        }
    }

    /**
     * Initializes the view components for the VerifyEmailView.
     * This method is responsible for finding and assigning the views to their respective variables.
     *
     * @param view The root view of the fragment.
     */
    private fun initializeViewComponents(view: View) {
        try {
            submitButton = view.findViewById(R.id.submit_button)
            exitButton = view.findViewById(R.id.exitPlaceholder)
            userGuideButton = view.findViewById(R.id.helpIcon)
            customerSupportButton = view.findViewById(R.id.customerSupportIcon)
            emailCodeField = view.findViewById(R.id.code_verify)
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error initializing view components", e)
        }
    }
}

