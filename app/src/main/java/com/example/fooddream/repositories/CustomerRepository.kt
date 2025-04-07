import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Customer
import com.google.gson.Gson
import androidx.core.content.edit
import com.example.fooddream.messengers.Notification

class CustomerRepository(private var view: AppCompatActivity) {

    private var notification = Notification()
    private val sharedPreferences: SharedPreferences =
        view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    //https://youtu.be/1gOG60SLSvg

    fun saveCustomer(customer: Customer) {
        val customerJson = gson.toJson(customer)
        sharedPreferences.edit() {
            putString("customer_key", customerJson)
        }
    }

    fun getCustomer(): Customer? {
        val customerJson = sharedPreferences.getString("customer_key", null)
        return if (customerJson != null) {
            gson.fromJson(customerJson, Customer::class.java)
        } else {
            null
        }
    }
}
