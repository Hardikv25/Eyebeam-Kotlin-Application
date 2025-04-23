package com.example.Eyebeam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Eyebeam.Adapter.CartAdapter
import com.example.Eyebeam.Helper.ChangeNumberItemsListener
import com.example.Eyebeam.Helper.ManagmentCart
import com.example.Eyebeam.databinding.ActivityCartBinding
import com.google.gson.Gson


class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagmentCart
    private var tax: Double = 0.0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("OrderDetails", MODE_PRIVATE)
        managementCart = ManagmentCart(this)
        setVariable()
        initCartList()
        calculateCart()
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 25.0
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100
        val itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100

        with(binding) {
            totalFeeTxt.text = "₹$itemTotal"
            taxTxt.text = "₹$tax"
            deliveryTxt.text = "₹$delivery"
            totaltxt.text = "₹$total"
        }
        // Get product names from the cart items, checking for null items
        val productNames = managementCart.getListCart()
            .filterNotNull()
            .joinToString(", ") { it.title }

        val checkout = findViewById<Button>(R.id.checkOutBtn)

        checkout.setOnClickListener {
            // Save in shared preferences

            sharedPreferences.edit().apply {
                putString("TotalAmount", total.toString())
                putString("ItemTotal", itemTotal.toString())
                putString("Tax", tax.toString())
                putString("DeliveryCharge", delivery.toString())
                putString("ProductNames", productNames)  // Store product names
                apply()
            }

            // Start AddressDetails activity
            val intent = Intent(this@CartActivity, AddressDetails::class.java)
            startActivity(intent)
        }
    }

    private fun initCartList() {
        binding.cartView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.cartView.adapter = CartAdapter(managementCart.getListCart(), this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCart()
            }
        })
    }

    private fun setVariable() {
        binding.backbtn.setOnClickListener {
            finish()
        }
    }
}
