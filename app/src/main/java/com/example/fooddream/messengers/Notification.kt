package com.example.fooddream.messengers

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.example.fooddream.R

class Notification {
    fun sendNotification(message: String, view: Activity) {
        try {
            Toast.makeText(view, message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Notification", "Error sending notification: ${e.message}")
        }
    }
    //https://ansarali-edugaon.medium.com/create-custom-alert-dialog-with-a-custom-view-in-android-kotlin-48e7dc8ce54f
    fun sendDeletePrompt(view: Activity, onDecision: (Boolean) -> Unit) {
        try {
            val inflater = LayoutInflater.from(view)
            val viewPrompt = inflater.inflate(R.layout.deleteprompt_layout, null)

            val deleteButton = viewPrompt.findViewById<Button>(R.id.dialogDelete_button)
            val cancelButton = viewPrompt.findViewById<Button>(R.id.dialogCancel_button)

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