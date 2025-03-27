package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddream.R
import com.example.fooddream.adapters.CustomerCatalogAdapter
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.models.Ingredient
import com.example.fooddream.models.Product

class CustomerCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var sessionController: SessionController
    private var allergens = mutableListOf<String>("Cheese")
    private var ingredient = Ingredient(
        ingredientName = "ged",
        ingredientId = 1,
        weight = 1.2,
        allergens = allergens,
        productId = 1
    )
    private var ingredients = mutableListOf<Ingredient>(ingredient)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_catalog)

        init()

    }

    private fun init() {
        sessionController = SessionController(this)
        recyclerView = findViewById(R.id.customer_catalog_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        productList = ArrayList()
        addDataToList()

        productAdapter = CustomerCatalogAdapter(productList)
        recyclerView.adapter = productAdapter
    }

    private fun addDataToList(){
        productList.add(Product(
            "Cheese",
            1,
            productPrice = 2.1,
            productCO = "UK",
            productStock = 1,
            productDescription = "ged",
            productCategory = "ged",
            imageURL = R.drawable.logo,
            ingredients = ingredients
        ))
    }
    private fun setListeners() {

    }

    override fun onPause() {
        super.onPause()
        sessionController.clearUserSession()
        Log.d("LoginView", "Session cleared on pause.")
    }

    override fun onStop() {
        super.onStop()
        sessionController.clearUserSession()
        Log.d("LoginView", "Session cleared on stop.")
    }
}