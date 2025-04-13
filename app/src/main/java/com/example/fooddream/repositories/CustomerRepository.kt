import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Customer
import com.google.gson.Gson
import androidx.core.content.edit

/**
 * CustomerRepository is responsible for managing customer data in the application.
 * It provides methods to save, retrieve, update, and delete customer information using SharedPreferences.
 *
 * @param view The activity context used for SharedPreferences operations.
 */
class CustomerRepository(private var view: AppCompatActivity) {

    /**
     * SharedPreferences instance to store customer data.
     * This instance is used to save and retrieve customer information using JSON serialization.
     */
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    //https://youtu.be/1gOG60SLSvg

    /**
     * Saves a customer object to SharedPreferences.
     * The customer object is serialized to JSON and stored in SharedPreferences.
     *
     * @param customer The customer object to be saved.
     *
     * @throws Exception if an error occurs while saving the customer.
     */
    fun saveCustomer(customer: Customer) {
        try {
            val customerJson = gson.toJson(customer)
            sharedPreferences.edit() {
                putString("customer_key", customerJson)
            }
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error saving customer: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Retrieves a customer object from SharedPreferences.
     * The customer object is deserialized from JSON stored in SharedPreferences.
     *
     * @return The retrieved customer object, or null if not found.
     *
     * @throws Exception if an error occurs while retrieving the customer.
     */
    fun getCustomer(): Customer? {
        try {
            val customerJson = sharedPreferences.getString("customer_key", null)
            return if (customerJson != null) {
                gson.fromJson(customerJson, Customer::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error retrieving customer: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Deletes the customer object from SharedPreferences.
     * This method removes the customer data stored in SharedPreferences.
     *
     * @throws Exception if an error occurs while deleting the customer.
     */
    fun deleteCustomer() {
        try {
            sharedPreferences.edit() {
                remove("customer_key")
            }
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error deleting customer: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Updates the customer object in SharedPreferences.
     * The updated customer object is serialized to JSON and stored in SharedPreferences.
     *
     * @param fName The first name of the customer.
     * @param lName The last name of the customer.
     * @param email The email address of the customer.
     * @param password The password of the customer.
     *
     * @throws Exception if an error occurs while updating the customer.
     */
    fun updateCustomer(fName: String, lName: String, email: String, password: String) {
        try {
            val customer = getCustomer()
            if (customer != null) {
                customer.setFName(fName)
                customer.setLName(lName)
                customer.setEmail(email)
                customer.setPassword(password)

                val updatedJson = gson.toJson(customer)
                sharedPreferences.edit {
                    putString("customer_key", updatedJson)
                }
            } else {
                Log.e("CustomerRepository", "Error updating customer: Customer not found")
            }
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error updating customer: ${e.message}")
            e.printStackTrace()
        }
    }
}
