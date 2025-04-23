package com.example.Eyebeam.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Adapter.AdminCategoryAdapter
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageCategory : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cateArrayList: ArrayList<AdminCategoryModel> // Correct model type
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_category)

        val back = findViewById<Button>(R.id.returndashboardcate)
        back.setOnClickListener {
            val intval = Intent(this@ManageCategory, Dashboard::class.java)
            startActivity(intval)
        }


        val addCategory = findViewById<Button>(R.id.addcategory)
        addCategory.setOnClickListener {
            val intval = Intent(this@ManageCategory, AddCategory::class.java)
            startActivity(intval)
        }

        recyclerView = findViewById(R.id.recycle_catedetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        cateArrayList = arrayListOf()

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Category")

        // Retrieve user data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        // Retrieve CustomerModel object from the snapshot
                        val category = dataSnapShot.getValue(AdminCategoryModel::class.java)
                        if (category != null && !cateArrayList.contains(category)) {
                            cateArrayList.add(category)
                        }
                    }
                    recyclerView.adapter = AdminCategoryAdapter(cateArrayList, database,this@ManageCategory) // Pass the database reference

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageCategory, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }
}
