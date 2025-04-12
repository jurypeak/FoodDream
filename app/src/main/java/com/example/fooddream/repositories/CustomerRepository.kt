import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Customer
import com.google.gson.Gson
import androidx.core.content.edit

class CustomerRepository(private var view: AppCompatActivity) {

    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    //https://youtu.be/1gOG60SLSvg

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
