package com.example.fooddream.messengers

class CustomerSupport {
    var notification = Notification()
    fun submitTicket(email: String, message: String) {
        notification.sendEmail(email, message)
    }
}