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
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.CustomerSupport
import com.example.fooddream.messengers.Notification

class CustomerSupportView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailField: EditText
    private lateinit var messageField: EditText
    private lateinit var supportButton: ImageView
    private lateinit var navigationController: NavigationController
    private lateinit var customerSupport: CustomerSupport
    private lateinit var notification: Notification
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.customer_support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        customerSupport = CustomerSupport()
        notification = Notification()

        init(view)
    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()
    }

    private fun initializeViewComponents(view: View) {
        try {
            submitButton = view.findViewById(R.id.submit_button)
            exitButton = view.findViewById(R.id.exitPlaceholder)
            emailField = view.findViewById(R.id.email_support)
            messageField = view.findViewById(R.id.support_message)
            supportButton = view.findViewById(R.id.helpIcon)
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", requireActivity() as AppCompatActivity)
            Log.d("Customer Support Error", "Error loading customer support: ${e.message}")
        }
    }
    private fun setListeners() {
        try {
            exitButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            supportButton.setOnClickListener {
                navigationController.navigateToUserGuide(urlUserGuide)
            }
            submitButton.setOnClickListener {
                customerSupport.submitTicket(
                    emailField.text.toString(),
                    messageField.text.toString(),
                    requireActivity() as AppCompatActivity
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occured loading customer support.", requireActivity() as AppCompatActivity)
            Log.d("Customer Support Error", "Error setting up listeners: ${e.message}")
        }
    }
}