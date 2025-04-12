package com.example.fooddream.messengers

import android.app.Activity
import android.util.Log

class CustomerSupport {
    var notification = Notification()

    fun submitTicket(email: String, message: String, view: Activity) {
        try {
            if (email.isNotEmpty() && message.isNotEmpty()) {
                notification.sendNotification("Ticket submitted successfully!", view)
            } else {
                notification.sendNotification("Please fill in all fields.", view)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while submitting the ticket.", view)
            Log.e("CustomerSupport", "Error submitting ticket: ${e.message}")
        }
    }
}