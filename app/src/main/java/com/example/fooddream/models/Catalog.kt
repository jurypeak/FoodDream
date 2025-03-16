package com.example.fooddream.models

class Catalog (
    private var products: List<Product>
) {
    //Getters
    fun getProducts(): List<Product> = products

    //Setters
    fun setProducts(newProducts: List<Product>) {
        products = newProducts
    }
}