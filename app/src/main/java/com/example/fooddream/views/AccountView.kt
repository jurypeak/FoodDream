package com.example.fooddream.views

import android.icu.text.NumberFormat
import androidx.fragment.app.Fragment
import android.os.Bundle
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
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.OrderController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.repositories.BasketRepository
import java.util.Locale

class AccountView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var orderController: OrderController
    private lateinit var basketRepository: BasketRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.account_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        orderController = OrderController(requireActivity() as AppCompatActivity)
        basketRepository = BasketRepository(requireActivity() as AppCompatActivity)

        init(view)

    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()
    }

    private fun initializeViewComponents(view: View) {
    }

    private fun setListeners() {
    }
}