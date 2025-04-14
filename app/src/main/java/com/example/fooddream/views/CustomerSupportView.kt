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
import com.example.fooddream.controllers.viewControllers.CustomerSupportViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.CustomerSupport
import com.example.fooddream.messengers.Notification

/**
 * CustomerSupportView is a Fragment that handles the user interface for customer support.
 * It allows users to enter their email and message to contact customer support.
 *
 * @property submitButton Button for submitting the support request.
 * @property exitTextView TextView for the exit button.
 * @property emailField EditText for entering the email address.
 * @property messageField EditText for entering the support message.
 * @property supportButton ImageView to access the help section.
 *
 * @property navigationController Controller for managing navigation actions.
 * @property customerSupport CustomerSupport object for handling customer support actions.
 * @property notification Notification manager for displaying messages to the user.
 * @property customerSupportViewController Controller for managing the customer support view logic.
 */
class CustomerSupportView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitTextView: TextView
    private lateinit var emailField: EditText
    private lateinit var messageField: EditText
    private lateinit var supportButton: ImageView

    private lateinit var navigationController: NavigationController
    private lateinit var customerSupport: CustomerSupport
    private lateinit var notification: Notification
    private lateinit var customerSupportViewController: CustomerSupportViewController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.customer_support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initialize the customer support view.
     * This method is responsible for initializing the view components and setting up the UI actions.
     * It also initializes the controllers and handles any exceptions that may occur during the process.
     *
     * @throws Exception if an error occurs while initializing the view components or setting up the UI actions.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeViewComponents(view)
        initializeControllers()
        setupUIActions()
    }

    /**
     * Initialize the view components for the customer support screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeControllers() {
        try {
            navigationController = NavigationController(requireActivity() as AppCompatActivity)
            customerSupport = CustomerSupport()
            notification = Notification()

            customerSupportViewController = CustomerSupportViewController(
                navigationController,
                notification,
                customerSupport,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", requireActivity() as AppCompatActivity)
            Log.d("Customer Support Error", "Error loading customer support: ${e.message}")
        }
    }

    /**
     * Set up the UI actions for the customer support screen.
     * This method is responsible for setting up click listeners for various UI components.
     * It also handles any exceptions that may occur during the setup process.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setupUIActions() {
        try {
            customerSupportViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                emailField,
                exitTextView,
                submitButton,
                supportButton,
                messageField,
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", requireActivity() as AppCompatActivity)
            Log.d("Customer Support Error", "Error loading customer support: ${e.message}")
        }
    }

    /**
     * Initialize the view components for the customer support screen.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @throws Exception if an error occurs while initializing the view components.
     *
     * @param view The root view of the fragment.
     */
    private fun initializeViewComponents(view: View) {
        try {
            submitButton = view.findViewById(R.id.submit_button)
            exitTextView = view.findViewById(R.id.exitPlaceholder)
            emailField = view.findViewById(R.id.email_support)
            messageField = view.findViewById(R.id.support_message)
            supportButton = view.findViewById(R.id.helpIcon)
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", requireActivity() as AppCompatActivity)
            Log.d("Customer Support Error", "Error loading customer support: ${e.message}")
        }
    }
}