package com.example.fooddream.controllers.viewControllers

import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.views.CustomerSupportView
import com.example.fooddream.views.LoginView
import com.example.fooddream.views.ResetPasswordEmailView

/**
 * ThreeDotsViewController is responsible for handling the three dots menu logic.
 * It sets up click listeners for various UI components and manages navigation.
 *
 * @property navigationController The controller for managing navigation between views.
 * @property notification The notification manager for displaying messages to the user.
 */
class ThreeDotsViewController(
    private val navigationController: NavigationController,
    private val notification: Notification
) {

    /**
     * Sets up click listeners for various UI components in the three dots menu.
     *
     * @param view The activity where the three dots menu is displayed.
     * @param forgotPasswordTextView The TextView for navigating to the reset password page.
     * @param customerSupportTextView The TextView for navigating to customer support.
     * @param userGuideTextView The TextView for navigating to the user guide.
     * @param logOutTextView The TextView for logging out the user.
     *
     * @throws Exception if an error occurs while setting up the click listeners.
     */
    fun setupClickListeners(
        view: AppCompatActivity,
        forgotPasswordTextView: TextView,
        customerSupportTextView: TextView,
        userGuideTextView: TextView,
        logOutTextView: TextView
    ) {
        try {
            forgotPasswordTextView.setOnClickListener {
                navigationController.navigateToFragment(ResetPasswordEmailView(), R.id.fragment_container)
            }
            customerSupportTextView.setOnClickListener {
                navigationController.navigateToFragment(CustomerSupportView(), R.id.fragment_container)
            }
            userGuideTextView.setOnClickListener {
                navigationController.navigateToUserGuide(BuildConfig.URL_USERGUIDE)
            }
            logOutTextView.setOnClickListener {
                val sessionController = SessionController(view)
                sessionController.clearUserSession()
                navigationController.navigateToActivity(LoginView::class.java)
            }
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error setting listeners: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", view)
        }
    }
}