package com.example.fooddream.views

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.fooddream.R

class VerifyEmailView : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var exitButton: TextView
    private lateinit var emailCodeField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var view = inflater.inflate(R.layout.verify_email, container, false)

        initializeViewComponents(view)

        return view
    }

    private fun initializeViewComponents(view: View) {
        submitButton = view.findViewById(R.id.submit_button)
        exitButton = view.findViewById(R.id.exitPlaceholder)
        emailCodeField = view.findViewById(R.id.code_verify)
    }
}
