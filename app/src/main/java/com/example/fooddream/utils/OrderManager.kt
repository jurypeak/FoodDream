package com.example.fooddream.utils

import CustomerRepository
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fooddream.controllers.NavigationController
import com.example.fooddream.messengers.Errors
import com.example.fooddream.messengers.Notification
import com.example.fooddream.models.Address
import com.example.fooddream.models.Order
import com.example.fooddream.models.OrderItem
import com.example.fooddream.models.Payment
import com.example.fooddream.repositories.AddressRepository
import com.example.fooddream.repositories.BasketRepository
import com.example.fooddream.repositories.OrderItemRepository
import com.example.fooddream.repositories.OrderRepository
import com.example.fooddream.repositories.PaymentRepository
import com.example.fooddream.views.CustomerCatalogView
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

/**
 * OrderManager is responsible for managing the order process in the application.
 * It handles order placement, order items, address, and payment processing.
 * This class interacts with the server to perform order-related operations.
 * It uses Volley for network requests and handles JSON responses.
 *
 * @param view The activity context used for order management.
 */
class OrderManager(
    private val view: AppCompatActivity,
    private val customerRepository: CustomerRepository,
) {
    private val basketRepository = BasketRepository(view)
    private val navigationController = NavigationController(view)
    private var notification = Notification()
    private var orderId = 0

    /**
     * Handles the order placement process.
     *
     * This method sends a JSON object to the server with the order details,
     * including email, first name, last name, and account ID.
     * It also handles the order items, address, and payment processing.
     *
     * @param email The email address of the user.
     * @param fName The first name of the user.
     * @param lName The last name of the user.
     * @param address The shipping address for the order.
     * @param town The town for the shipping address.
     * @param postcode The postcode for the shipping address.
     * @param paymentMethod The payment method selected by the user.
     * @param requestQueue The Volley request queue for network operations.
     * @param urlOrder The URL endpoint for placing the order on the server.
     * @param urlPayment The URL endpoint for processing the payment on the server.
     * @param urlAddress The URL endpoint for saving the address on the server.
     * @param urlOrderItems The URL endpoint for saving the order items on the server.
     *
     * @throws Exception if an error occurs while placing the order.
     */
    fun handleOrder(
        email: String,
        fName: String,
        lName: String,
        address: String,
        town: String,
        postcode: String,
        paymentMethod: String,
        requestQueue: RequestQueue,
        urlOrder: String,
        urlPayment: String,
        urlAddress: String,
        urlOrderItems: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("email", email)
                put("fName", fName)
                put("lName", lName)
                put("accountId", customerRepository.getCustomer()?.getAccountId())
            }
            Log.d("Order", "$email, $fName, $lName, ${customerRepository.getCustomer()?.getAccountId()}")
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlOrder, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        orderId = response.optInt("orderId")
                        handleOrderItems(
                            requestQueue,
                            urlOrderItems
                        )
                        handleAddress(
                            address,
                            town,
                            postcode,
                            requestQueue,
                            urlAddress
                        )
                        handlePayment(
                            paymentMethod,
                            requestQueue,
                            urlPayment
                        )
                        basketRepository.clearBasket()
                        notification.sendNotification("Order placed successfully", view)
                        Log.d("Response", "$response")
                        navigationController.navigateToActivity(CustomerCatalogView::class.java)
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: kotlin.Exception) {
            Log.d("Order Error", "$error")
        }
    }

    /**
     * Handles the order items by sending them to the server.
     *
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for saving the order items on the server.
     *
     * @throws Exception if an error occurs while handling order items.
     */
    fun handleOrderItems(
        requestQueue: RequestQueue,
        url: String,
    ) {
        for (basketItem in basketRepository.getAllBasketItems(customerRepository.getCustomer()?.getAccountId())) {
            try {
                val jsonObject = JSONObject().apply {
                    put("orderId", orderId)
                    put("productId", basketItem.getProductId())
                    put("quantity", basketItem.getQuantity())
                    put("price", basketItem.getPrice())
                    put("productName", basketItem.getItemName())
                }
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    { response ->
                        val returnedResponseStatus = response.optString("status", "")
                        if (returnedResponseStatus == "Success") {
                            Log.d("Response", "$response")
                        } else {
                            notification.sendNotification("${response.optString("message", "")}", view)
                            Log.d("Response", "$response")
                        }
                    },
                    { error ->
                        notification.sendNotification(error.toString(), view)
                        Log.d("Volley Error", "$error")
                    })
                requestQueue.add(jsonObjectRequest)
            } catch (error: Exception) {
                Log.d("Order Error", "$error")
            }
        }
    }

    /**
     * Handles the address by sending it to the server.
     *
     * @param address The shipping address for the order.
     * @param town The town for the shipping address.
     * @param postcode The postcode for the shipping address.
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for saving the address on the server.
     *
     * @throws Exception if an error occurs while handling the address.
     */
    fun handleAddress(
        address: String,
        town: String,
        postcode: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("address", address)
                put("town", town)
                put("postcode", postcode)
                put("orderId", orderId)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Exception) {
            Log.d("Order Error", "$error")
        }
    }

    /**
     * Handles the payment by sending it to the server.
     *
     * @param paymentMethod The payment method selected by the user.
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for processing the payment on the server.
     *
     * @throws Exception if an error occurs while handling the payment.
     */
    fun handlePayment(
        paymentMethod: String,
        requestQueue: RequestQueue,
        url: String
    ) {
        try {
            val jsonObject = JSONObject().apply {
                put("paymentMethod", paymentMethod)
                put("amount", basketRepository.getBasketTotalPrice(customerRepository.getCustomer()?.getAccountId()))
                put("orderId", orderId)
            }
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    val returnedResponseStatus = response.optString("status", "")
                    if (returnedResponseStatus == "Success") {
                        Log.d("Response", "$response")
                    } else {
                        notification.sendNotification("${response.optString("message", "")}", view)
                        Log.d("Response", "$response")
                    }
                },
                { error ->
                    notification.sendNotification(error.toString(), view)
                    Log.d("Volley Error", "$error")
                })
            requestQueue.add(jsonObjectRequest)
        } catch (error: Exception) {
            Log.d("Order Error", "$error")
        }
    }

    /**
     * Fetches orders from the server and saves them to the local database.
     *
     * @param requestQueue The Volley request queue for network operations.
     * @param url The URL endpoint for fetching orders from the server.
     * @param accountId The ID of the account to fetch orders for.
     *
     * @throws Exception if an error occurs while fetching orders.
     */
    fun getOrders(
        requestQueue: RequestQueue,
        url: String,
        accountId: Int?,
    ) {
        try {
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val orderRepository = OrderRepository(view)
                        val orderItemRepository = OrderItemRepository(view)
                        val addressRepository = AddressRepository(view)
                        val paymentRepository = PaymentRepository(view)

                        for (i in 0 until response.length()) {
                            val orderJson = response.getJSONObject(i)

                            val orderId = orderJson.getInt("id")
                            val orderAccountId = orderJson.getInt("accountId")
                            val orderFName = orderJson.getString("fName")
                            val orderLName = orderJson.getString("lName")
                            val orderEmail = orderJson.getString("email")
                            val orderDate = orderJson.getString("date")

                            val order = Order(
                                orderFName,
                                orderLName,
                                orderEmail,
                                orderAccountId,
                                orderId,
                                orderDate
                            )

                            val orderItemsArray = orderJson.getJSONArray("orderItems")
                            val orderItems = ArrayList<OrderItem>()

                            for (j in 0 until orderItemsArray.length()) {
                                val orderItemJson = orderItemsArray.getJSONObject(j)

                                val orderItemsId = orderItemJson.getInt("id")
                                val productId = orderItemJson.getInt("productId")
                                val orderItemQuantity = orderItemJson.getInt("quantity")
                                val orderItemPrice = orderItemJson.getDouble("price")
                                val orderItemName = orderItemJson.getString("itemName")

                                orderItems.add(
                                    OrderItem(
                                        orderItemsId,
                                        productId,
                                        orderId,
                                        orderItemQuantity,
                                        orderItemPrice,
                                        orderItemName
                                    )
                                )
                            }

                            val payments = ArrayList<Payment>()
                            if (!orderJson.isNull("payment")) {
                                val paymentJson = orderJson.getJSONObject("payment")

                                val paymentId = paymentJson.getInt("id")
                                val paymentMethod = paymentJson.getString("paymentMethod")
                                val paymentDate = paymentJson.getString("paymentDate")
                                val paymentAmount = paymentJson.getDouble("amount")

                                payments.add(
                                    Payment(
                                        paymentId,
                                        orderId,
                                        paymentMethod,
                                        paymentDate,
                                        paymentAmount
                                    )
                                )
                            }

                            val addresses = mutableListOf<Address>()
                            if (!orderJson.isNull("address")) {
                                val addressJson = orderJson.getJSONObject("address")

                                val addressId = addressJson.getInt("id")
                                val addressStreet = addressJson.getString("street")
                                val addressTown = addressJson.getString("town")
                                val addressPostcode = addressJson.getString("postcode")

                                addresses.add(
                                    Address(
                                        addressId,
                                        orderId,
                                        addressStreet,
                                        addressPostcode,
                                        addressTown
                                    )
                                )
                            }

                            if (orderAccountId == accountId) {
                                orderRepository.saveOrder(accountId, order)
                                orderItemRepository.saveOrderItem(orderId, orderItems)
                                addressRepository.saveAddresses(orderId, addresses)
                                paymentRepository.savePayments(orderId, payments)
                            }
                        }
                    } catch (e: JSONException) {
                        Log.e("Volley Error", "JSON parsing error: $e")
                    }
                },
                { error ->
                    Log.e("Volley Error", "Error: ${error.message}")
                }
            )
            requestQueue.add(jsonArrayRequest)
        } catch (error: Exception) {
            Log.e("Product Fetch Error", "$error")
        }
    }
}