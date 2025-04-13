package com.example.fooddream.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

// https://youtu.be/BXejFpaEwRc

/**
 * SessionManager class to manage user sessions.
 * This class provides methods to start, check, and clear user sessions.
 *
 * @param view The AppCompatActivity instance used for context.
 */
class SessionManager(view: AppCompatActivity) {

    /**
     * SharedPreferences instance to manage user session data.
     * This instance is used to store and retrieve session-related information.
     *
     * @see SharedPreferences
     */
    private var prefs: SharedPreferences = view.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    /**
     * Starts a user session.
     * This method sets the session to active and logs the action.
     *
     * @param isLoggedIn Boolean indicating whether the user is logged in.
     *
     * @throws Exception if an error occurs during session management.
     */
    fun startSession(isLoggedIn: Boolean) {
        try {
            prefs.edit() {
                putBoolean("isLoggedIn", isLoggedIn)
            }
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

    /**
     * Checks if a user session is active.
     * This method returns true if the session is active, false otherwise.
     *
     * @return Boolean indicating whether the user session is active.
     *
     * @throws Exception if an error occurs during session management.
     */
    fun hasSession(): Boolean {
        try {
            return prefs.getBoolean("isLoggedIn", false)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Clears the user session.
     * This method sets the session to inactive and logs the action.
     *
     * @throws Exception if an error occurs during session management.
     */
    fun clearSession() {
        try {
            prefs.edit() {
                remove("isLoggedIn")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
