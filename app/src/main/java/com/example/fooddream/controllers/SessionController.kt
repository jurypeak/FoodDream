package com.example.fooddream.controllers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.utils.SessionManager

class SessionController(private val view: AppCompatActivity) {

    private val sessionManager = SessionManager(view)

    fun startUserSession() {
        sessionManager.startSession(true)
        Log.d("SessionController", "User session started.")
    }

    fun clearUserSession() {
        sessionManager.clearSession()
        Log.d("SessionController", "User session cleared.")
    }
}
