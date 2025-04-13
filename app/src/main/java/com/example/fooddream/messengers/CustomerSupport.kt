package com.example.fooddream.messengers

import android.app.Activity
import android.util.Log
import com.example.fooddream.utils.ValidateManager

/**
 * CustomerSupport class handles the submission of customer support tickets.
 * It validates the input fields and sends notifications to the user.
 *
 * @constructor Creates an instance of CustomerSupport.
 *
 * @property notification An instance of Notification to handle user notifications.
 * @property validateManager An instance of ValidateManager to validate user input.
 *
 * @throws Exception if an error occurs during ticket submission.
 */
class CustomerSupport() {
    var notification = Notification()
    var validateManager = ValidateManager()

    fun submitTicket(email: String, message: String, view: Activity) {
        try {
            if (email.isEmpty() && message.isEmpty()) {
                notification.sendNotification("All fields are required", view)
            }
            if (!validateManager.isValidEmail(email)) {
                notification.sendNotification("Invalid email address.", view)
            }
            if (!validateManager.isValidMessage(message)) {
                notification.sendNotification("Message cannot be empty.", view)
            }
            else {
                // Simulate ticket submission
                notification.sendNotification("Ticket submitted successfully!", view)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while submitting the ticket.", view)
            Log.e("CustomerSupport", "Error submitting ticket: ${e.message}")
        }
    }
}