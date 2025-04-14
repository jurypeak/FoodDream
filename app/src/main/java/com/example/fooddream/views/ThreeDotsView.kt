package com.example.fooddream.views

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.ThreeDotsViewController
import com.example.fooddream.messengers.Notification

/**
 * ThreeDotsView is a Fragment that displays a menu with options for the user.
 * It allows the user to access features such as resetting their password, customer support,
 * user guide, and logging out.
 *
 * @property navigationController The controller for managing navigation between views.
 * @property forgotPasswordTextView TextView for the "Forgot Password" option.
 * @property customerSupportTextView TextView for the "Customer Support" option.
 * @property userGuideTextView TextView for the "User Guide" option.
 * @property logOutTextView TextView for the "Log Out" option.
 * @property notification The notification manager for displaying messages to the user.
 * @property threeDotsViewController The controller for managing the three dots view logic.
 */
class ThreeDotsView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var customerSupportTextView: TextView
    private lateinit var userGuideTextView: TextView
    private lateinit var logOutTextView: TextView
    private lateinit var notification: Notification
    private lateinit var threeDotsViewController: ThreeDotsViewController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dots_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the ThreeDotsView by setting up the controllers, view components, and UI actions.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        setUIActions()
    }

    /**
     * Initializes the controllers for managing navigation and notifications.
     *
     * @param view The activity context for initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            navigationController = NavigationController(view)
            notification = Notification()

            threeDotsViewController = ThreeDotsViewController(
                navigationController,
                notification
            )
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error initializing controllers: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Sets up the click listeners for the UI components.
     * This method is responsible for setting up the click listeners for the various options in the three dots menu.
     *
     * @throws Exception if an error occurs while setting up the UI actions.
     */
    private fun setUIActions() {
        try {
            threeDotsViewController.setupClickListeners(
                requireActivity() as AppCompatActivity,
                forgotPasswordTextView,
                customerSupportTextView,
                userGuideTextView,
                logOutTextView
            )
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error setting UI actions: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Initializes the view components for the ThreeDotsView.
     * This method is responsible for finding and assigning the views to their respective variables.
     *
     * @param view The root view of the fragment.
     */
    private fun initializeViewComponents(view: View) {
        try {
            forgotPasswordTextView = view.findViewById(R.id.forgot_password_3dots)
            customerSupportTextView = view.findViewById(R.id.customer_support_3dots)
            userGuideTextView = view.findViewById(R.id.user_guide_3dots)
            logOutTextView = view.findViewById(R.id.log_out_3dots)
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error initializing view components: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", requireActivity() as AppCompatActivity)
        }
    }
}