package com.example.fooddream.interfaces

import com.android.volley.RequestQueue

interface IAccountController {
    fun setHashedPassword(password: String): Boolean
    fun login(email: String,
              password: String,
              requestQueue: RequestQueue,
              url: String
    )
    fun logout(sessionId: Int): Boolean
}