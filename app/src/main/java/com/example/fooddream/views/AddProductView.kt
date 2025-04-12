package com.example.fooddream.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.Volley
import com.example.fooddream.R
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification

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

        productController = ProductController(requireActivity() as AppCompatActivity)
        navigationController = NavigationController(requireActivity() as AppCompatActivity)
        notification = Notification()

        init(view)

    }

    private fun init(view: View) {
        initializeViewComponents(view)
        setUpListeners()
    }

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

    @SuppressLint("SetTextI18n")
    private fun setUpListeners() {
        try {
            addIngredientButton.setOnClickListener {
                val row = TableRow(requireContext())

                val nameInput = EditText(requireContext()).apply {
                    hint = "Name"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }
                val weightInput = EditText(requireContext()).apply {
                    hint = "Weight (g)"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    maxLines = 1

                }
                val allergensInput = EditText(requireContext()).apply {
                    hint = "Allergens"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }
                val removeButton = Button(requireContext()).apply {
                    text = "Remove"
                    layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
                    setOnClickListener {
                        ingredientTable.removeView(row)
                    }
                }

                row.addView(nameInput)
                row.addView(weightInput)
                row.addView(allergensInput)
                row.addView(removeButton)

                ingredientTable.addView(row)
            }

            addProductButton.setOnClickListener {
                if (
                    imageURL.text.isEmpty() ||
                    productNameEditView.text.isEmpty() ||
                    productDescriptionEditView.text.isEmpty() ||
                    productPriceEditView.text.isEmpty() ||
                    productCategoryEditView.text.isEmpty() ||
                    productStockEditView.text.isEmpty() ||
                    productCOEditView.text.isEmpty()
                ) {
                    notification.sendNotification(
                        "Please fill in all fields",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                }

                if (ingredientTable.childCount <= 1) {
                    notification.sendNotification(
                        "Please add at least one ingredient",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                }

                if (productPriceEditView.text.toString().toDouble() <= 0) {
                    notification.sendNotification(
                        "Price must be greater than 0",
                        requireActivity() as AppCompatActivity
                    )
                    return@setOnClickListener
                } else {
                    productController.addProduct(
                        imageURL.text.toString(),
                        productNameEditView.text.toString(),
                        productDescriptionEditView.text.toString(),
                        productPriceEditView.text.toString().toDouble(),
                        productCategoryEditView.text.toString(),
                        productStockEditView.text.toString().toInt(),
                        productCOEditView.text.toString(),
                        productController.collectIngredients(ingredientTable)
                            ?: return@setOnClickListener,
                        BuildConfig.URL_ADD_PRODUCT,
                        BuildConfig.URL_ADD_INGREDIENT,
                        Volley.newRequestQueue(requireContext()),
                        notification
                    )
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading add product page.", requireActivity() as AppCompatActivity)
            Log.e("AddProductView", "Error setting up listeners", e)
        }
    }
}