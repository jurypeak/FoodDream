package com.example.fooddream.controllers

import com.example.fooddream.models.Catalog
import com.example.fooddream.models.Manager
import com.example.fooddream.models.Product

class AdminCatalogController (
    private var manager: Manager,
    private var catalog: Catalog,
    private var catalogController: CatalogController
) {
    fun addProduct(newProduct: Product) {

    }
    fun deleteProduct(productId: Int) {

    }
    fun editProduct(
        newName: String,
        newCategory: String,
        newPrice: Double,
        newDescription: String,
        newImageURL: String
    ) {

    }
    fun displayProducts() {

    }
    fun searchProducts(query: String): List<Product>? {
        return null
    }
    fun filterProducts(category: String): List<Product>? {
        return null
    }
    fun viewProduct(productId: Int): Product? {
        return null
    }
}