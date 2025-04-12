package com.example.fooddream.views

import CustomerRepository
import android.icu.text.NumberFormat
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

class ThreeDotsView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var customerSupportTextView: TextView
    private lateinit var userGuideTextView: TextView
    private lateinit var logOutTextView: TextView
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dots_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()
    }

    private fun initializeViewComponents(view: View) {
        try {
            forgotPasswordTextView = view.findViewById(R.id.forgot_password_3dots)
            customerSupportTextView = view.findViewById(R.id.customer_support_3dots)
            userGuideTextView = view.findViewById(R.id.user_guide_3dots)
            logOutTextView = view.findViewById(R.id.log_out_3dots)
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error initializing view components: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", requireActivity() as AppCompatActivity)
        }
    }

    private fun setListeners() {
        try {
            forgotPasswordTextView.setOnClickListener {
                navigationController.navigateToFragment(ResetPasswordEmailView(), R.id.fragment_container)
            }
            customerSupportTextView.setOnClickListener {
                navigationController.navigateToFragment(CustomerSupportView(), R.id.fragment_container)
            }
            userGuideTextView.setOnClickListener {
                navigationController.navigateToUserGuide(BuildConfig.URL_USERGUIDE)
            }
            logOutTextView.setOnClickListener {
                val sessionController = SessionController(requireActivity() as AppCompatActivity)
                sessionController.clearUserSession()
                navigationController.navigateToActivity(LoginView::class.java)
            }
        } catch (e: Exception) {
            Log.e("ThreeDotsView", "Error setting listeners: ${e.message}")
            notification.sendNotification("Error occurred while loading the miscellaneous menu.", requireActivity() as AppCompatActivity)
        }
    }
}