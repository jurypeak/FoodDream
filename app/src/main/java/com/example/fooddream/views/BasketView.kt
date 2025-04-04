package com.example.fooddream.views

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.models.Product

class BasketView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.basket_page, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationController = NavigationController(requireActivity() as AppCompatActivity)

        init(view)

    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList(view: View, searchQuery: String?) {
        productController.getProductsInDB(
            Volley.newRequestQueue(requireContext()),
            BuildConfig.URL_PRODUCTS,
            searchQuery
        ) { products ->
            if (products != null) {
                productList.clear()
                productList.addAll(products)
                productAdapter.notifyDataSetChanged()
            } else {
                Log.e("Product Display Error", "Failed to display products.")
            }
        }
    }

    private fun initializeViewComponents(view: View) {
    }

    private fun setListeners() {
    }
}