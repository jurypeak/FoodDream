import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddream.models.Product
import com.google.gson.Gson
import androidx.core.content.edit
import com.example.fooddream.messengers.Notification

class ProductRepository(private var view: AppCompatActivity) {

    private var notification = Notification()
    private val sharedPreferences: SharedPreferences = view.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveProduct(product: Product) {
        val productJson = gson.toJson(product)
        sharedPreferences.edit() {
            putString("product_${product.getProductId()}", productJson)
        }
    }

    fun getProduct(productId: Int): Product? {
        val productJson = sharedPreferences.getString("product_$productId", null)
        return if (productJson != null) {
            gson.fromJson(productJson, Product::class.java)
        } else {
            null
        }
    }

    fun getAllProducts(): ArrayList<Product> {
        val allProducts = ArrayList<Product>()
        val keys = sharedPreferences.all.keys
        for (key in keys) {
            if (key.startsWith("product_")) {
                val productJson = sharedPreferences.getString(key, null)
                if (productJson != null) {
                    val product = gson.fromJson(productJson, Product::class.java)
                    allProducts.add(product)
                }
            }
        }
        return allProducts
    }

    fun removeProduct(productId: Int) {
        sharedPreferences.edit() {
            remove("product_$productId")
        }
    }
}
