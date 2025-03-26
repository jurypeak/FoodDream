package com.example.fooddream.views

import android.content.ActivityNotFoundException
import android.content.Intent
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
import androidx.core.net.toUri
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.CustomerController

class VerifyEmailView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailCodeField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView
    private lateinit var controller: CustomerController
    private var urlUserGuide = BuildConfig.URL_USERGUIDE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.verify_email_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = CustomerController(requireActivity() as AppCompatActivity)
        val email = arguments?.getString("email") ?: ""

        initializeViewComponents(view)
        setListeners()

        controller.sendVerificationEmailCode(
            email,
            Volley.newRequestQueue(requireActivity() as AppCompatActivity),
            BuildConfig.URL_VERIFY_EMAIL,
        )
    }

    private fun initializeViewComponents(view: View) {
        submitButton = view.findViewById(R.id.submit_button)
        exitButton = view.findViewById(R.id.exitPlaceholder)
        emailCodeField = view.findViewById(R.id.code_verify)
        userGuideButton = view.findViewById(R.id.helpIcon)
        customerSupportButton = view.findViewById(R.id.customerSupportIcon)
    }
    private fun setListeners() {
        exitButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        userGuideButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, urlUserGuide.toUri())
            intent.setPackage("com.android.chrome")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (error: ActivityNotFoundException) {
                Log.e("Chrome Error", "$error")
                intent.setPackage(null)
                startActivity(intent)
            }
        }
       customerSupportButton.setOnClickListener {
            controller.createCustomerSupportView()
       }
    }
}

