package com.example.fooddream.messengers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.example.fooddream.R

/**
 * Notification class for sending notifications and prompts.
 *
 * This class provides methods to send notifications and delete prompts to the user.
 * It handles exceptions that may occur during the process and logs them for debugging purposes.
 */
class Notification {

    /**
     * Sends a toast notification message to the user.
     *
     * @param message The message to be displayed in the notification.
     * @param view The activity context used for displaying the notification.
     *
     * @throws Exception if an error occurs while sending the notification.
     */
    fun sendNotification(message: String, view: Activity) {
        try {
            Toast.makeText(view, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Notification", "Error sending notification: ${e.message}")
        }
    }

    //https://ansarali-edugaon.medium.com/create-custom-alert-dialog-with-a-custom-view-in-android-kotlin-48e7dc8ce54f
    /**
     * Sends a delete product prompt to the admin.
     *
     * @param view The activity context used for displaying the prompt.
     * @param onDecision A callback function that receives the user's decision (true for delete, false for cancel).
     *
     * @throws Exception if an error occurs while sending the delete prompt.
     */

    @SuppressLint("MissingInflatedId")
    fun sendDeleteProductPrompt(view: Activity, onDecision: (Boolean) -> Unit) {
        try {
            val inflater = LayoutInflater.from(view)
            val viewPrompt = inflater.inflate(R.layout.deleteproductprompt_layout, null)

            val deleteButton = viewPrompt.findViewById<Button>(R.id.dialogDeleteProduct_button)
            val cancelButton = viewPrompt.findViewById<Button>(R.id.dialogCancelProduct_button)

            val builder = AlertDialog.Builder(view, R.style.CustomAlertDialog)
            builder.setView(viewPrompt)

            val dialog = builder.create()

            deleteButton.setOnClickListener {
                dialog.dismiss()
                onDecision(true)
            }

            cancelButton?.setOnClickListener {

                dialog.dismiss()
                onDecision(false)
            }

            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        } catch (e: Exception) {
            sendNotification("Error occurred while loading the delete prompt.", view)
            Log.e("Notification", "Error sending delete prompt: ${e.message}")
        }
    }

    fun sendDeleteAccountPrompt(view: Activity, onDecision: (Boolean) -> Unit) {
        try {
            val inflater = LayoutInflater.from(view)
            val viewPrompt = inflater.inflate(R.layout.deleteaccountprompt_layout, null)

            val deleteButton = viewPrompt.findViewById<Button>(R.id.dialogDeleteAccount_button)
            val cancelButton = viewPrompt.findViewById<Button>(R.id.dialogCancelAccount_button)

            val builder = AlertDialog.Builder(view, R.style.CustomAlertDialog)
            builder.setView(viewPrompt)

            val dialog = builder.create()

            deleteButton.setOnClickListener {
                dialog.dismiss()
                onDecision(true)
            }

            cancelButton?.setOnClickListener {

                dialog.dismiss()
                onDecision(false)
            }

            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        } catch (e: Exception) {
            sendNotification("Error occurred while loading the delete prompt.", view)
            Log.e("Notification", "Error sending delete prompt: ${e.message}")
        }
    }
}