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
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController

class CustomerSupportView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var messageField: EditText
    private lateinit var supportButton: ImageView
    private lateinit var navigationController: NavigationController
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

        initializeViewComponents(view)
        setListeners()

    }

    private fun initializeViewComponents(view: View) {
        submitButton = view.findViewById(R.id.submit_button)
        exitButton = view.findViewById(R.id.exitPlaceholder)
        messageField = view.findViewById(R.id.support_message)
        supportButton = view.findViewById(R.id.helpIcon)
    }
    private fun setListeners() {
        exitButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        supportButton.setOnClickListener {
            navigationController.navigateToUserGuide(urlUserGuide)
        }
    }
}