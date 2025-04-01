package com.example.fooddream.views

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController

class CustomerSearchCatalogView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var searchBar: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.search_catalog_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)

        initializeViewComponents(view)
        setListeners()

    }

    private fun initializeViewComponents(view: View) {
        searchBar = view.findViewById(R.id.search_bar)
    }
    private fun setListeners() {

    }
}