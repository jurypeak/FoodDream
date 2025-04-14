package com.example.fooddream.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fooddream.R
import com.example.fooddream.controllers.viewControllers.AddProductViewController
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification

/**
 * AddProductView is a Fragment that allows the admin to add a new product.
 * It contains fields for product details and a button to submit the product.
 *
 * @property ingredientTable TableLayout for displaying ingredients.
 * @property addIngredientButton Button to add ingredients.
 * @property addProductButton Button to submit the product.
 * @property imageURL EditText for entering the product image URL.
 * @property productNameEditView EditText for entering the product name.
 * @property productDescriptionEditView EditText for entering the product description.
 * @property productPriceEditView EditText for entering the product price.
 * @property productCategoryEditView EditText for entering the product category.
 * @property productStockEditView EditText for entering the product stock.
 * @property productCOEditView EditText for entering the product CO (Country of Origin).
 *
 * @property productController Controller for managing product-related actions.
 * @property addProductViewController Controller for managing the add product view logic.
 * @property navigationController Controller for managing navigation actions.
 * @property notification Notification manager for displaying messages to the user.
 */

class AddProductView : Fragment() {
    private lateinit var ingredientTable: TableLayout
    private lateinit var addIngredientButton: Button
    private lateinit var addProductButton: Button
    private lateinit var imageURL: EditText
    private lateinit var productNameEditView: EditText
    private lateinit var productDescriptionEditView: EditText
    private lateinit var productPriceEditView: EditText
    private lateinit var productCategoryEditView: EditText
    private lateinit var productStockEditView: EditText
    private lateinit var productCOEditView: EditText

    private lateinit var productController: ProductController
    private lateinit var addProductViewController: AddProductViewController
    private lateinit var navigationController: NavigationController
    private lateinit var notification: Notification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    /**
     * Initializes the AddProductView by setting up controllers, view components, and UI actions.
     *
     * @param view The root view of the fragment.
     */
    private fun init(view: View) {
        initializeControllers(requireActivity() as AppCompatActivity)
        initializeViewComponents(view)
        setUIActions()
    }

    /**
     * Initializes the controllers for managing product-related actions and navigation.
     * This method is responsible for creating instances of the controllers and handling any exceptions that may occur during the process.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     *
     * @param context The context of the activity.
     *
     * This method attempts to create instances of the ProductController, NavigationController,
     * and Notification classes.
     *
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the controllers.
     */
    private fun initializeControllers(context: AppCompatActivity) {
        try {
            productController = ProductController(context)
            navigationController = NavigationController(context)
            notification = Notification()

            addProductViewController = AddProductViewController(
                productController,
                notification
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading add product page.", context)
            Log.e("AddProductView", "Error initializing controllers", e)
        }
    }

    /**
     * Sets up the UI actions for the AddProductView.
     * This method is responsible for setting up click listeners and handling user interactions.
     *
     * @throws Exception if an error occurs while setting up UI actions.
     */
    private fun setUIActions() {
        try {
            addProductViewController.setupClickListeners(
                requireContext(),
                requireActivity() as AppCompatActivity,
                addIngredientButton,
                addProductButton,
                imageURL,
                productNameEditView,
                productDescriptionEditView,
                productPriceEditView,
                productCategoryEditView,
                productStockEditView,
                productCOEditView,
                ingredientTable
            )
        } catch (e: Exception) {
            notification.sendNotification("Error while loading add product page.", requireActivity() as AppCompatActivity)
            Log.e("AddProductView", "Error setting up UI actions", e)
        }
    }

    /**
     * Initializes the view components for the AddProductView.
     * This method is responsible for finding and assigning the views to their respective variables.
     * It also handles any exceptions that may occur during the initialization process.
     *
     * @param view The root view of the fragment.
     *
     * This method attempts to find and initialize the view components used in the AddProductView.
     * If an exception occurs, it sends a notification to the user and logs the error.
     *
     * @throws Exception if an error occurs while initializing the view components.
     */
    private fun initializeViewComponents(view: View) {
        try {
            ingredientTable = view.findViewById(R.id.ingredient_table)

            addIngredientButton = view.findViewById(R.id.add_ingredient_button)
            addProductButton = view.findViewById(R.id.addButton)

            imageURL = view.findViewById(R.id.imageURL_add_product)
            productNameEditView = view.findViewById(R.id.productName_add_product)
            productDescriptionEditView = view.findViewById(R.id.productDescription_add_product)
            productPriceEditView = view.findViewById(R.id.productPrice_add_product)
            productCategoryEditView = view.findViewById(R.id.productCategory_add_product)
            productStockEditView = view.findViewById(R.id.productStock_add_product)
            productCOEditView = view.findViewById(R.id.productCO_add_product)
        } catch (e: Exception) {
            notification.sendNotification("Error while loading add product page.", requireActivity() as AppCompatActivity)
            Log.e("AddProductView", "Error initializing view components", e)
        }
    }
}