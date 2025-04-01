package com.example.fooddream.controllers

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

class NavigationController(private val view: AppCompatActivity) {

    fun replaceActivityWithFragment(fragment: Fragment, containerId: Int) {
        Log.d("Navigation", "Attempting to navigate to ${fragment::class.java.simpleName}")
        val transaction = view.supportFragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        Log.d("Navigation", "Fragment transaction committed for ${fragment::class.java.simpleName}")
    }

    fun navigateToFragment(view: Fragment, fragmentId: Int) {
        replaceActivityWithFragment(view, fragmentId)
    }

    fun navigateToActivity(activityClass: Class<*>) {
        view.startActivity(Intent(view, activityClass))
        view.finish()
    }

    fun navigateToUserGuide(urlUserGuide: String) {
        val intent = Intent(Intent.ACTION_VIEW, urlUserGuide.toUri())
        intent.setPackage("com.android.chrome")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            view.startActivity(intent)
        } catch (error: ActivityNotFoundException) {
            Log.e("Chrome Error", "$error")
            intent.setPackage(null)
            view.startActivity(intent)
        }
    }
}