package com.example.Eyebeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.Adapter.WishlistAdapter
import com.example.Eyebeam.Model.WishlistModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WishlistActivity : AppCompatActivity() {
    private lateinit var adapter: WishlistAdapter
    private lateinit var wishlistItems: MutableList<WishlistModel>
    private val sharedPrefs by lazy { getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE) }
    private val gson by lazy { Gson() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)


        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        back_arrow.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

        wishlistItems = getWishlistItems() // Load wishlist items from SharedPreferences
        adapter = WishlistAdapter(wishlistItems) { item -> removeFromWishlist(item) }

        val recyclerView: RecyclerView = findViewById(R.id.wishlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun removeFromWishlist(item: WishlistModel) {
        wishlistItems.remove(item)
        saveWishlistItems(wishlistItems) // Update the stored list
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "${item.title} removed from wishlist", Toast.LENGTH_SHORT).show()
    }               

    private fun getWishlistItems(): MutableList<WishlistModel> {
        val json = sharedPrefs.getString("wishlist_items", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<WishlistModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveWishlistItems(items: List<WishlistModel>) {
        val editor = sharedPrefs.edit()
        val json = gson.toJson(items)
        editor.putString("wishlist_items", json)
        editor.apply()
    }
}
