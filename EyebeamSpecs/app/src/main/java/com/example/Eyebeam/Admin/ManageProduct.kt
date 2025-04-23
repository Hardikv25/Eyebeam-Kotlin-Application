package com.example.Eyebeam.Admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Adapter.AdminCategoryAdapter
import com.example.Eyebeam.com.example.Eyebeam.Adapter.AdminProductAdapter
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCategoryModel
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageProduct : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var prodArrayList: ArrayList<AdminProductModel> // Correct model type
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_product)

        val back = findViewById<Button>(R.id.returndashboardprod)
        back.setOnClickListener {
            val intval = Intent(this@ManageProduct, Dashboard::class.java)
            startActivity(intval)
        }

        recyclerView = findViewById(R.id.recycle_proddetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = AdminProductAdapter(prodArrayList) // Make sure adapter is set after data is loaded


        prodArrayList = arrayListOf()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("items")

        // Retrieve user data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prodArrayList.clear()
                for (postSnapshot in snapshot.children) {
                    val product = postSnapshot.getValue(AdminProductModel::class.java)
                    if (product != null) {
                        prodArrayList.add(product)
                    }
                }
                recyclerView.adapter = AdminProductAdapter(prodArrayList) // Set adapter here
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ManageProduct", "Database error: ${error.message}")
            }
        })
    }
}
