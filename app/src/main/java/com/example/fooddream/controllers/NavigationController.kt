package com.example.fooddream.controllers

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

/**
 * NavigationController is responsible for handling navigation within the application.
 * It provides methods to navigate between fragments and activities, as well as to open the user guide.
 *
 * @param view The AppCompatActivity instance used for navigation.
 */
class NavigationController(private val view: AppCompatActivity) {

    /**
     * Helper function to navigate to a new fragment.
     *
     * @param fragment The Fragment instance used for navigation.
     * @param containerId The ID of the container where the fragment will be replaced.
     *
     * @throws Exception if an error occurs during navigation.
     */
    fun replaceActivityWithFragment(fragment: Fragment, containerId: Int) {
        try {
            Log.d("Navigation", "Attempting to navigate to ${fragment::class.java.simpleName}")

            val transaction = view.supportFragmentManager.beginTransaction()

            transaction.replace(containerId, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

            Log.d("Navigation", "Fragment transaction committed for ${fragment::class.java.simpleName}")
        } catch (e: Exception) {
            Log.e("Navigation Error", "Error navigating to fragment: ${e.message}")
        }
    }

    /**
     * Replaces the current activity with a new fragment.
     *
     * @param view The Fragment instance used for navigation.
     * @param fragmentId The ID of the container where the fragment will be replaced.
     */
    fun navigateToFragment(view: Fragment, fragmentId: Int) {
        replaceActivityWithFragment(view, fragmentId)
    }

    /**
     * Navigates to a new activity.
     *
     * @param activityClass The class of the activity to navigate to.
     */
    fun navigateToActivity(activityClass: Class<*>) {
        try {
            view.startActivity(Intent(view, activityClass))
            view.finish()
        } catch (e: Exception) {
            Log.e("Navigation Error", "Error navigating to activity: ${e.message}")
        }
    }

    /**
     * Navigates to the user guide URL in the default web browser.
     *
     * @param urlUserGuide The URL of the user guide.
     */
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