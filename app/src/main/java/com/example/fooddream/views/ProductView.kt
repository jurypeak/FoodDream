package com.example.fooddream.views

import com.example.fooddream.repositories.ProductRepository
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.R
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.viewControllers.ProductViewController
import com.example.fooddream.messengers.Notification
import com.example.fooddream.repositories.IngredientRepository

/**
 * ProductView is a Fragment that displays the details of a specific product.
 * It allows the user to view product information such as name, description, price, stock, and ingredients.
 *
 * @property navigationController The controller for managing navigation between views.
 * @property productRepository The repository for managing product data.
 * @property ingredientRepository The repository for managing ingredient data.
 * @property productViewController The controller for managing the product view.
 * @property notification The notification manager for displaying messages to the user.
 * @property productImageView ImageView for displaying the product image.
 * @property productNameTextView TextView for displaying the product name.
 * @property productDescriptionTextView TextView for displaying the product description.
 * @property productPriceTextView TextView for displaying the product price.
 * @property productStockTextView TextView for displaying the product stock.
 * @property productIngredientTextView TextView for displaying the list of ingredients.
 * @property productCOTextView TextView for displaying the country of origin (CO) of the product.
 */
class ProductView : Fragment() {

    private lateinit var navigationController: NavigationController
    private lateinit var productRepository: ProductRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var productViewController: ProductViewController
    private lateinit var notification: Notification
    private lateinit var productImageView: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productStockTextView: TextView
    private lateinit var productIngredientTextView: TextView
    private lateinit var productCOTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.product_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the ProductView by setting up the necessary components and controllers.
     *
     * @param view The view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view.
     */
    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        try {
            var productId = arguments?.getInt("ProductId") ?: -1
            Log.d("ProductView", "Product ID: $productId")

            initializeControllers(requireActivity() as AppCompatActivity)
            initializeViewComponents(view)
            productViewController.initializeProductScreen(
                requireActivity() as AppCompatActivity,
                productId,
                productImageView,
                productNameTextView,
                productDescriptionTextView,
                productPriceTextView,
                productStockTextView,
                productIngredientTextView,
                productCOTextView,
                ingredientRepository,
                productRepository,
                notification
            )
        } catch (e: Exception) {
            Log.e("ProductView", "Error initializing product view: ${e.message}")
            notification.sendNotification("Error occurred while loading product view.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Initializes the controllers used in the ProductView.
     * This method is responsible for creating instances of the necessary controllers and repositories.
     *
     * @param view The activity context used to initialize the controllers.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(view: AppCompatActivity) {
        try {
            navigationController = NavigationController(view)
            productRepository = ProductRepository(view)
            ingredientRepository = IngredientRepository(view)
            notification = Notification()

            productViewController = ProductViewController()
        } catch (e: Exception) {
            Log.e("ProductView", "Error initializing controllers: ${e.message}")
            notification.sendNotification("Error occurred while loading product view.", requireActivity() as AppCompatActivity)
        }
    }

    /**
     * Initializes the view components used in the ProductView.
     * This method is responsible for finding and initializing the UI elements.
     *
     * @param view The root view of the fragment.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents(view: View) {
        try {
            productImageView = view.findViewById(R.id.productImageView)
            productNameTextView = view.findViewById(R.id.productNameTextView)
            productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView)
            productPriceTextView = view.findViewById(R.id.productPriceTextView)
            productStockTextView = view.findViewById(R.id.productStockTextView)
            productIngredientTextView = view.findViewById(R.id.productIngredientsTextView)
            productCOTextView = view.findViewById(R.id.productCOTextView)
        } catch (e: Exception) {
            notification.sendNotification("Error while initializing product view components.", requireActivity() as AppCompatActivity)
            Log.e("ProductView", "Error initializing view components: ${e.message}")
        }
    }
}