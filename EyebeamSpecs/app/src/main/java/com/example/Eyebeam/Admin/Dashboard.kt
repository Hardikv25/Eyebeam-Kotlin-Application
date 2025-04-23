package com.example.Eyebeam.Admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.Eyebeam.LoginActivity
import com.example.Eyebeam.R


class Dashboard : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val managecust = findViewById<CardView>(R.id.card_manage_customer)

        managecust.setOnClickListener {
            val intval = Intent(this@Dashboard, ManageCustomer::class.java)
            startActivity(intval)
        }


        val managecategory = findViewById<CardView>(R.id.card_manage_category)

        managecategory.setOnClickListener {
            val intvalcategory = Intent(this@Dashboard, ManageCategory::class.java)
            startActivity(intvalcategory)
        }

        val manageproduct = findViewById<CardView>(R.id.card_manage_product)

        manageproduct.setOnClickListener {
            val intvalproduct = Intent(this@Dashboard, ManageProduct::class.java)
            startActivity(intvalproduct)
        }

        val manageorder = findViewById<CardView>(R.id.card_manage_order)

        manageorder.setOnClickListener {
            val intvalorder = Intent(this@Dashboard, ManageOrder::class.java)
            startActivity(intvalorder)
        }

        val btnlogout = findViewById<Button>(R.id.btnadminlogout)

        btnlogout.setOnClickListener {
            val intvalproduct = Intent(this@Dashboard, LoginActivity::class.java)
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
            startActivity(intvalproduct)
        }


    }
}