package com.example.fooddream.views

import com.example.fooddream.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.fooddream.controllers.AccountController
import com.example.fooddream.models.Account

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var model: Account =getDataFromDb()
        var view=this

        var controller= AccountController(
            model,
            view
        )
        printDetails(model.getEmail())
        var button : Button = findViewById(R.id.btn)
        button.setOnClickListener(View.OnClickListener {
            model.setEmail("acsa")
            printDetails(model.getEmail());
        })
    }

    private fun getDataFromDb(): Account {
        return Account(
            "ged",
            2,
            2,
            "d"
        )
    }

    private fun printDetails(email: String){
        Log.d("Account", "email $email")
    }
}