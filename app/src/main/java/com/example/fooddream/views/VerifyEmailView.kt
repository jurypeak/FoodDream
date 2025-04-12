package com.example.fooddream.views

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Notification

class VerifyEmailView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailCodeField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView
    private lateinit var accountController: AccountController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification
    private var urlUserGuide = BuildConfig.URL_USERGUIDE
    private var urlEmailVerify = BuildConfig.URL_VERIFY_EMAIL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.verify_email_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        accountController = AccountController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()

        try {
            var email = arguments?.getString("email") ?: ""
            var typeView = arguments?.getString("typeView") ?: ""

            accountController.sendTwoFactorAuth(
                email,
                Volley.newRequestQueue(requireActivity() as AppCompatActivity),
                urlEmailVerify,
                typeView
            )
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error initializing view components", e)
        }
    }

    private fun initializeViewComponents(view: View) {
        try {
            submitButton = view.findViewById(R.id.submit_button)
            exitButton = view.findViewById(R.id.exitPlaceholder)
            userGuideButton = view.findViewById(R.id.helpIcon)
            customerSupportButton = view.findViewById(R.id.customerSupportIcon)
            emailCodeField = view.findViewById(R.id.code_verify)
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error initializing view components", e)
        }
    }
    private fun setListeners() {
        try {
            exitButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            userGuideButton.setOnClickListener {
            }
            customerSupportButton.setOnClickListener {
                navigationController.navigateToUserGuide(urlUserGuide)
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occured while loading verify email page.", requireActivity() as AppCompatActivity)
            Log.e("VerifyEmailView", "Error setting up listeners", e)
        }
    }
}

