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
import com.example.fooddream.adapters.AdminCatalogAdapter
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.controllers.SessionController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Product

class AdminCatalogView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: AdminCatalogAdapter
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification
    private lateinit var homeButton: ImageView
    private lateinit var searchButton: ImageView
    private lateinit var plusButton: ImageView
    private lateinit var threeDotsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.admin_catalog_page)

        init()
    }

    private fun init() {
        try {
            sessionController = SessionController(this)
            productController = ProductController(this)
            navigationController = NavigationController(this)
            notification = Notification()

            recyclerView = findViewById(R.id.admin_catalog_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 2)

            productList = ArrayList()
            productAdapter = AdminCatalogAdapter(
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
                    notification.sendDeleteProductPrompt(this) { confirmed ->
                        if (confirmed) {
                            Log.d("DeleteProduct", "Deleting product: ${product.getProductName()}")
                            productController.removeProduct(
                                product.getProductId(),
                                BuildConfig.URL_DELETE_PRODUCT,
                                Volley.newRequestQueue(this),
                                notification,
                                navigationController
                            )
                        } else {
                            Log.d("DeleteProduct", "Deletion cancelled for product: ${product.getProductName()}")
                        }
                    }
                },
                { product ->
                    val bundle = Bundle().apply {
                        putInt("ProductId", product.getProductId())
                    }
                    val editProductViewFragment = EditProductView().apply {
                        arguments = bundle
                    }
                    navigationController.navigateToFragment(
                        editProductViewFragment,
                        R.id.fragment_container
                    )
                }
            )
            recyclerView.adapter = productAdapter
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$error")
        }

        addDataToList()
        initializeViewComponents()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToList() {
        try {
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
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    private fun initializeViewComponents() {
        try {
            homeButton = findViewById(R.id.home_button)
            searchButton = findViewById(R.id.search_button)
            plusButton = findViewById(R.id.plus_button)
            threeDotsButton = findViewById(R.id.dots_button)
        } catch (error: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$error")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        try {
            homeButton.setOnClickListener {
                homeButton.setImageResource(R.drawable.house_red)
                searchButton.setImageResource(R.drawable.search)
                plusButton.setImageResource(R.drawable.plus)
                threeDotsButton.setImageResource(R.drawable.dots)
                navigationController.navigateToActivity(AdminCatalogView::class.java)
            }
            searchButton.setOnClickListener {
                searchButton.setImageResource(R.drawable.search_red)
                plusButton.setImageResource(R.drawable.plus)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    AdminSearchCatalogView(),
                    R.id.fragment_container
                )
            }
            plusButton.setOnClickListener {
                plusButton.setImageResource(R.drawable.plus_red)
                searchButton.setImageResource(R.drawable.search)
                homeButton.setImageResource(R.drawable.house)
                threeDotsButton.setImageResource(R.drawable.dots)
                productList.clear()
                productAdapter.notifyDataSetChanged()
                navigationController.navigateToFragment(
                    AddProductView(),
                    R.id.fragment_container
                )
            }
            threeDotsButton.setOnClickListener {
                threeDotsButton.setImageResource(R.drawable.dots_red)
                searchButton.setImageResource(R.drawable.search)
                plusButton.setImageResource(R.drawable.plus)
                homeButton.setImageResource(R.drawable.house)
                productList.clear()
                navigationController.navigateToFragment(
                    ThreeDotsView(),
                    R.id.fragment_container
                )
            }
        } catch (e: Exception) {
            notification.sendNotification("Error occurred while loading the catalog.", this)
            Log.d("Catalog Initialization Error", "$e")
        }
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