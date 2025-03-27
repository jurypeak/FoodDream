package com.example.fooddream.models

open class Product (
    private var productName: String,
    private var productId: Int,
    private var productPrice: Double,
    private var productCO: String,
    private var productStock: Int,
    private var productDescription: String,
    private var productCategory: String,
    private var imageUrl: String,
    internal var ingredients: MutableList<Ingredient> = mutableListOf()
) {
    // Getters
    fun getProductName(): String = productName
    fun getProductId(): Int = productId
    fun getProductPrice(): Double = productPrice
    fun getProductCO(): String = productCO
    fun getProductStock(): Int = productStock
    fun getProductDescription(): String = productDescription
    fun getProductCategory(): String = productCategory
    fun getImageUrl(): String = imageUrl
    fun getIngredients(): List<Ingredient> = ingredients

    //Setters
    fun setProductName(newProductName: String) {
        productName = newProductName
    }
    fun setProductId(newProductId: Int) {
        productId = newProductId
    }
    fun setProductPrice(newProductPrice: Double) {
        productPrice = newProductPrice
    }
    fun setProductCO(newProductCO: String) {
        productCO = newProductCO
    }
    fun setProductStock(newProductStock: Int) {
        productStock = newProductStock
    }
    fun setProductDescription(newProductDescription: String) {
        productDescription = newProductDescription
    }
    fun setProductCategory(newProductCategory: String) {
        productCategory = newProductCategory
    }
    fun setImageUrl(newImageURL: String) {
        imageUrl = newImageURL
    }
    fun addIngredients(newIngredient: Ingredient) {
        ingredients.add(newIngredient)
    }
}