package com.example.Eyebeam.Admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.Adapter.AdminCustomerAdapter
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCustomerModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageCustomer : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<AdminCustomerModel>
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_customer)

        val back = findViewById<Button>(R.id.returndashboardcust)
        back.setOnClickListener {
            val intval = Intent(this@ManageCustomer, Dashboard::class.java)
            startActivity(intval)
        }

        recyclerView = findViewById(R.id.recycle_custdetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("users")

        // Retrieve user data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear() // Clear existing data
                    for (dataSnapShot in snapshot.children) {
                        // Retrieve CustomerModel object from the snapshot
                        val user = dataSnapShot.getValue(AdminCustomerModel::class.java)
                        if (user != null) {
                            userArrayList.add(user)
                        }
                    }
                    // Set adapter with the updated userArrayList
                    recyclerView.adapter = AdminCustomerAdapter(userArrayList, database,this@ManageCustomer) // Pass the database reference
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageCustomer, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
