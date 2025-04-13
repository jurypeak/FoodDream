package com.example.fooddream.controllers

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.utils.SessionManager

/**
 * SessionController is responsible for managing user sessions in the application.
 * It provides methods to start, check, and clear user sessions.
 *
 * @param view The activity context used for session management.
 */
class SessionController(private val view: AppCompatActivity) {

    /**
     * SessionManager instance to manage user sessions.
     * This instance is used to start, check, and clear user sessions.
     */
    private val sessionManager = SessionManager(view)

    /**
     * Starts a user session.
     * This method sets the session to active and logs the action.
     */
    fun startUserSession() {
        sessionManager.startSession(true)
        Log.d("SessionController", "User session started.")
    }

    /**
     * Checks if a user session is active.
     * This method returns true if the session is active, false otherwise.
     *
     * @return Boolean indicating whether the user session is active.
     */
    fun hasUserSession(): Boolean {
        return sessionManager.hasSession()
    }

    /**
     * Clears the user session.
     * This method sets the session to inactive and logs the action.
     */
    fun clearUserSession() {
        sessionManager.clearSession()
        Log.d("SessionController", "User session cleared.")
    }
}
