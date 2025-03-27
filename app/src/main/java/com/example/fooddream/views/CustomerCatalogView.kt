package com.example.fooddream.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.R
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.models.Product

class CustomerCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_catalog)

        init()

    }

    private fun init() {
        sessionController = SessionController(this)
        productController = ProductController()

        recyclerView = findViewById(R.id.customer_catalog_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        productList = ArrayList()
        productAdapter = CustomerCatalogAdapter(productList)
        recyclerView.adapter = productAdapter

        addDataToList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        productController.getProductsInDB(
            Volley.newRequestQueue(this),
            BuildConfig.URL_PRODUCTS
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
    private fun setListeners() {

    }

    override fun onPause() {
        super.onPause()
        sessionController.clearUserSession()
        Log.d("LoginView", "Session cleared on pause.")
    }

//    override fun onStop() {
//        super.onStop()
//        sessionController.clearUserSession()
//        Log.d("LoginView", "Session cleared on stop.")
//    }
}