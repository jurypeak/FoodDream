package com.example.fooddream.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

// https://youtu.be/BXejFpaEwRc

class SessionManager(view: AppCompatActivity) {
    private var prefs: SharedPreferences = view.getSharedPreferences("user_session", Context.MODE_PRIVATE)

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

    fun hasSession(): Boolean {
        try {
            return prefs.getBoolean("isLoggedIn", false)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

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
