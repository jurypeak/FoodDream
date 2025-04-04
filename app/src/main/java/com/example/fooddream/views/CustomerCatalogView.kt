package com.example.fooddream.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
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

class CustomerCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var navigationController: NavigationController
    private lateinit var homeButton: ImageView
    private lateinit var searchButton: ImageView
    private lateinit var basketButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.customer_catalog)

        init()
    }

    private fun init() {
        sessionController = SessionController(this)
        productController = ProductController(this)
        navigationController = NavigationController(this)

        recyclerView = findViewById(R.id.customer_catalog_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        productList = ArrayList()
        productAdapter = CustomerCatalogAdapter(
            this,
            productList,
            { product ->
                Log.d(
                    "RecyclerViewClick",
                    "Clicked product: ${product.getProductName()} with ID: ${product.getProductId()}"
                )
                val bundle = Bundle().apply {
                    putInt("ProductId", product.getProductId())
                }
                val productViewFragment = ProductView().apply {
                    arguments = bundle
                }
                navigationController.navigateToFragment(
                    productViewFragment,
                    R.id.fragment_container
                )
            },
            { product ->
                Log.d("AddToBasket", "Added product: ${product.getProductName()} to the basket")
            },
            { product ->
                Log.d(
                    "RemoveFromBasket",
                    "Removed product: ${product.getProductName()} from the basket"
                )
            },
            { product ->
                Log.d(
                    "IncrementQuantity",
                    "Incremented product quantity: ${product.getProductName()} in the basket"
                )
            }
        )
        recyclerView.adapter = productAdapter

        addDataToList()
        initializeViewComponents()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        productController.getProductsInDB(
            Volley.newRequestQueue(this),
            BuildConfig.URL_PRODUCTS,
            null
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

    private fun initializeViewComponents() {
        homeButton = findViewById(R.id.home_button)
        searchButton = findViewById(R.id.search_button)
        basketButton = findViewById(R.id.basket_button)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        homeButton.setOnClickListener {
            homeButton.setImageResource(R.drawable.house_red)
            basketButton.setImageResource(R.drawable.basket)
            searchButton.setImageResource(R.drawable.search)
            navigationController.navigateToActivity(CustomerCatalogView::class.java)
        }
        searchButton.setOnClickListener {
            searchButton.setImageResource(R.drawable.search_red)
            basketButton.setImageResource(R.drawable.basket)
            homeButton.setImageResource(R.drawable.house)
            productList.clear()
            productAdapter.notifyDataSetChanged()
            navigationController.navigateToFragment(
                CustomerSearchCatalogView(),
                R.id.fragment_container
            )
        }
        basketButton.setOnClickListener {
            basketButton.setImageResource(R.drawable.basket_red)
            searchButton.setImageResource(R.drawable.search)
            homeButton.setImageResource(R.drawable.house)
            productList.clear()
            productAdapter.notifyDataSetChanged()
            navigationController.navigateToFragment(
                BasketView(),
                R.id.fragment_container
            )
        }
    }

//    override fun onPause() {
//        super.onPause()
//        sessionController.clearUserSession()
//        Log.d("LoginView", "Session cleared on pause.")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        sessionController.clearUserSession()
//        Log.d("LoginView", "Session cleared on stop.")
//    }
}