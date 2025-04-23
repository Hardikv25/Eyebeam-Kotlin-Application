package com.example.Eyebeam

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Eyebeam.com.example.Eyebeam.Model.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetails : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var orderContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)


        val backorder = findViewById<ImageView>(R.id.orderback)

        backorder.setOnClickListener {
            val intval = Intent(this@OrderDetails,HomeActivity::class.java)
            startActivity(intval)

        }


        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("orders")

        orderContainer = findViewById(R.id.order_container)

        // Fetch orders for the logged-in user
        fetchUserOrders()
    }

    private fun fetchUserOrders() {
        // Get the current user's username from shared preferences
        val userSharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val username = userSharedPreferences.getString("username", "")

        // Check if username is valid
        if (!username.isNullOrEmpty()) {
            database.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (orderSnapshot in dataSnapshot.children) {
                                val order = orderSnapshot.getValue(OrderModel::class.java)
                                order?.let {
                                    displayOrder(it)
                                }
                            }
                        } else {
                            Toast.makeText(this@OrderDetails, "No orders found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@OrderDetails, "Error fetching orders: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayOrder(order: OrderModel) {
        try {
            val orderView = layoutInflater.inflate(R.layout.order_item, null)

            // Find views in order_item layout
            val orderDetailsTextView = orderView.findViewById<TextView>(R.id.order_details)

            // Set order details to the TextView
            orderDetailsTextView.text = "Customer: ${order.customerName}\n" +
                    "Phone: ${order.customerPhone}\n" +
                    "Total Amount: ${order.totalAmount}\n" +
                    "Items: ${order.productNames}\n"+
                    "Order Date : ${order.currentDate}"

            // Add the order view to the container
            orderContainer.addView(orderView)
        } catch (e: Exception) {
            Toast.makeText(this, "Error displaying order: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()  // This will log the error to Logcat
        }
    }

}
