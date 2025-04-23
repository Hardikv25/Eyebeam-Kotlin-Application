package com.example.Eyebeam.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.Adapter.AdminCustomerAdapter
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Adapter.AdminOrderAdapter
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCustomerModel
import com.example.Eyebeam.com.example.Eyebeam.Model.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageOrder : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderArrayList: ArrayList<OrderModel>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_order)
        val back = findViewById<Button>(R.id.returndashboardorder)
        back.setOnClickListener {
            val intval = Intent(this@ManageOrder, Dashboard::class.java)
            startActivity(intval)
        }

        recyclerView = findViewById(R.id.recycle_orderdetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        orderArrayList = arrayListOf()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("orders")

        // Retrieve user data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    orderArrayList.clear() // Clear existing data
                    for (dataSnapShot in snapshot.children) {
                        val order = dataSnapShot.getValue(OrderModel::class.java)
                        if (order != null) {
                            orderArrayList.add(order)
                        }
                    }
                    // Set adapter with the updated userArrayList
                    recyclerView.adapter = AdminOrderAdapter(orderArrayList, database,this@ManageOrder) // Pass the database reference
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageOrder, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}