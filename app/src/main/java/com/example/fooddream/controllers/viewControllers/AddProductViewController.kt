package com.example.fooddream.controllers.viewControllers

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.example.fooddream.BuildConfig
import com.example.fooddream.controllers.ProductController
import com.example.fooddream.messengers.Notification

/**
 * AddProductViewController is responsible for managing the view and interactions
 * related to adding a new product in the application.
 *
 * @property productController Controller for managing product-related actions.
 * @property notification Notification manager for displaying messages to the user.
 */
class AddProductViewController(
    private val productController: ProductController,
    private val notification: Notification
) {

    /**
     * Sets up click listeners for the buttons in the Add Product view.
     *
     * @param context The context of the activity.
     * @param view The activity where the buttons are located.
     * @param addIngredientButton Button to add an ingredient.
     * @param addProductButton Button to add a product.
     * @param imageURL EditText for entering the image URL.
     * @param productNameEditView EditText for entering the product name.
     * @param productDescriptionEditView EditText for entering the product description.
     * @param productPriceEditView EditText for entering the product price.
     * @param productCategoryEditView EditText for entering the product category.
     * @param productStockEditView EditText for entering the product stock.
     * @param productCOEditView EditText for entering the product CO (carbon monoxide).
     * @param ingredientTable TableLayout for displaying ingredients.
     *
     * @throws Exception if an error occurs while setting up the listeners.
     */
    @SuppressLint("SetTextI18n")
    fun setupClickListeners(
        context: Context,
        view: AppCompatActivity,
        addIngredientButton: Button,
        addProductButton: Button,
        imageURL: EditText,
        productNameEditView: EditText,
        productDescriptionEditView: EditText,
        productPriceEditView: EditText,
        productCategoryEditView: EditText,
        productStockEditView: EditText,
        productCOEditView: EditText,
        ingredientTable: TableLayout
    ) {
        try {
            addIngredientButton.setOnClickListener {
                val row = TableRow(context)

                val nameInput = EditText(context).apply {
                    hint = "Name"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }
                val weightInput = EditText(context).apply {
                    hint = "Weight (g)"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    maxLines = 1

                }
                val allergensInput = EditText(context).apply {
                    hint = "Allergens"
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    inputType = InputType.TYPE_CLASS_TEXT
                    maxLines = 1
                }
                val removeButton = Button(context).apply {
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
                        view
                    )
                    return@setOnClickListener
                }

                if (ingredientTable.childCount <= 1) {
                    notification.sendNotification(
                        "Please add at least one ingredient",
                        view
                    )
                    return@setOnClickListener
                }

                if (productPriceEditView.text.toString().toDouble() <= 0) {
                    notification.sendNotification(
                        "Price must be greater than 0",
                        view
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
                        Volley.newRequestQueue(context),
                        notification
                    )
                }
            }
        } catch (e: Exception) {
            notification.sendNotification("Error while loading add product page.", view)
            Log.e("AddProductView", "Error setting up listeners", e)
        }
    }
}