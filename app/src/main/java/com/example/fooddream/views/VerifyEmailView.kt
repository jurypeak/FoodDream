package com.example.fooddream.views

import androidx.fragment.app.Fragment
import android.os.Bundle
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

class VerifyEmailView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailCodeField: EditText
    private lateinit var userGuideButton: ImageView
    private lateinit var customerSupportButton: ImageView
    private lateinit var accountController: AccountController
    private lateinit var navigationController: NavigationController
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

        val email = arguments?.getString("email") ?: ""

        initializeViewComponents(view)
        setListeners()

        accountController.sendTwoFactorAuth(
            email,
            Volley.newRequestQueue(requireActivity() as AppCompatActivity),
            urlEmailVerify,
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
        }
       customerSupportButton.setOnClickListener {
            navigationController.navigateToUserGuide(urlUserGuide)
       }
    }
}

