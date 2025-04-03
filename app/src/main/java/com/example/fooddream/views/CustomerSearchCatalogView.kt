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

class CustomerSearchCatalogView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: CustomerCatalogAdapter
    private lateinit var navigationController: NavigationController
    private lateinit var sessionController: SessionController
    private lateinit var productController: ProductController
    private lateinit var searchBar: EditText

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

        init(view)

    }

    private fun init(view: View) {
        sessionController = SessionController(requireActivity() as AppCompatActivity)
        productController = ProductController(requireActivity() as AppCompatActivity)

        recyclerView = view.findViewById(R.id.customer_search_catalog_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireActivity() as AppCompatActivity, 2)

        productList = ArrayList()
        productAdapter = CustomerCatalogAdapter(
            requireActivity() as AppCompatActivity,
            productList,
            { product ->
                Log.d("RecyclerViewClick", "Clicked product: ${product.getProductName()} with ID: ${product.getProductId()}")
                val bundle = Bundle().apply {
                    putInt("ProductId", product.getProductId())
                }
                val productViewFragment = ProductView().apply {
                    arguments = bundle
                }
                navigationController.navigateToFragment(productViewFragment, R.id.fragment_container)
            },
            { product ->
                Log.d("AddToBasket", "Added product: ${product.getProductName()} to the basket")
            },
            { product ->
                Log.d("RemoveFromBasket", "Removed product: ${product.getProductName()} from the basket")
            },
            { product ->
                Log.d("IncrementQuantity", "Incremented product quantity: ${product.getProductName()} in the basket")
            }
        )
        recyclerView.adapter = productAdapter

        initializeViewComponents(view)
        addDataToList(view, searchBar.text.toString())
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
        searchBar = view.findViewById(R.id.search_bar)
    }

    //https://youtu.be/ONWE38Tm2_8

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private fun setListeners() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()

                searchRunnable?.let { handler.removeCallbacks(it) }

                searchRunnable = Runnable {
                    if (query.isNotEmpty()) {
                        Log.d("SearchBar", "Searching for: \"$query\"")
                        addDataToList(requireView(), query)
                    } else {
                        Log.d("SearchBar", "Empty query, loading all products")
                        addDataToList(requireView(), null)
                    }
                }

                handler.postDelayed(searchRunnable!!, 100)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}