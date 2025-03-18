package com.example.fooddream.messengers

import android.app.Activity
import android.widget.Toast

class Notification {
    fun sendEmail(email: String, message: String) {

    }
    fun sendNotification(message: String, view: Activity) {
        Toast.makeText(view, message, Toast.LENGTH_SHORT).show()
    }
}