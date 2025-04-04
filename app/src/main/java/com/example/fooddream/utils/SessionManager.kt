package com.example.fooddream.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

// https://youtu.be/BXejFpaEwRc

class SessionManager(view: AppCompatActivity) {
    private var prefs: SharedPreferences = view.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun startSession(isLoggedIn: Boolean) {
        prefs.edit() { putBoolean("isLoggedIn", isLoggedIn) }
    }

    fun hasSession(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    fun clearSession() {
        prefs.edit() { clear() }
    }
}
