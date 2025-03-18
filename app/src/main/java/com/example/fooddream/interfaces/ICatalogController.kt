package com.example.fooddream.interfaces

import com.example.fooddream.models.Product

interface ICatalogController {
    fun displayProducts()
    fun searchProducts(query: String): List<Product>?
    fun filterProducts(category: String): List<Product>?
    fun viewProduct(productId: Int): Product?
}