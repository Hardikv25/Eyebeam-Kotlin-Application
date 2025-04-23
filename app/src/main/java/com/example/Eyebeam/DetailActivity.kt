package com.example.Eyebeam

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.Eyebeam.Adapter.PicListAdapter
import com.example.Eyebeam.Adapter.SizeListAdapter
import com.example.Eyebeam.Helper.ManagmentCart
import com.example.Eyebeam.Model.ItemsModel
import com.example.Eyebeam.Model.WishlistModel
import com.example.Eyebeam.databinding.ActivityDetailBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ResourceBundle.getBundle

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagmentCart
    private lateinit var sizeListAdapter: SizeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val whishlist = findViewById<CheckBox>(R.id.Favicon)
//        whishlist.setOnClickListener {
//            if (whishlist.isChecked) {
//                showToast("Item added to Whishlist")
//            } else {
//                showToast("Item removed from Whishlist")
//            }
//        }

        managementCart = ManagmentCart(this)
        getBundle()
        initList()
        handleWishlist(item)
        setWishlistCheckboxState()
    }
    private fun setWishlistCheckboxState() {
        val wishlistCheckBox = findViewById<CheckBox>(R.id.Favicon)
        val currentList = getWishlistItems()

        // Check if the item is in the wishlist
        val isInWishlist = currentList.any { it.title == item.title && it.picUrl == item.picUrl.first() }
        wishlistCheckBox.isChecked = isInWishlist
    }
    private fun handleWishlist(item: ItemsModel) {
        val wishlistCheckBox = findViewById<CheckBox>(R.id.Favicon)
        wishlistCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                addToWishlist(item)
//                showToast("Added to Wishlist")
            } else {
                removeFromWishlist(item)
//                showToast("Removed from Wishlist")
            }
        }
    }
    private fun addToWishlist(item: ItemsModel) {
        // Create a wishlist item without an ID
        val wishlistItem = WishlistModel(item.title, item.price, item.picUrl.first())
        val currentList = getWishlistItems()

        // Check if the item already exists in the wishlist
        if (!currentList.any { it.title == wishlistItem.title && it.picUrl == wishlistItem.picUrl }) {
            currentList.add(wishlistItem)
            saveWishlistItems(currentList)
            showToast("Added to Wishlist")
        } else {
            showToast("Item is already in the Wishlist")
        }
    }
    private fun removeFromWishlist(item: ItemsModel) {
        val currentList = getWishlistItems().toMutableList()

        // Remove item based on title and picUrl
        currentList.removeAll { it.title == item.title && it.picUrl == item.picUrl.first() }
        saveWishlistItems(currentList)
        showToast("Removed from Wishlist")
    }

    private fun getWishlistItems(): MutableList<WishlistModel> {
        val sharedPreferences = getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("wishlist_items", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<WishlistModel>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
    private fun saveWishlistItems(items: List<WishlistModel>) {
        val sharedPreferences = getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(items)
        editor.putString("wishlist_items", json)
        editor.apply()
    }

    private fun showToast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    private fun initList() {
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size.toString())
        }

        sizeListAdapter = SizeListAdapter(sizeList)
        binding.sizeList.adapter = sizeListAdapter
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }
        Glide.with(this)
            .load(colorList[0])
            .into(binding.picMain)

        binding.picList.adapter = PicListAdapter(colorList, binding.picMain)
        binding.picList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "₹" + item.price
        binding.ratingTxt.text = "${item.rating}"
        binding.textView17.text = item.sellerName

        binding.AddToCartBtn.setOnClickListener {
            if (sizeListAdapter.selectedPosition == -1) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
            } else {
                item.numberInCart = numberOrder
                managementCart.insertItems(item)
                Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener { finish() }
        binding.CartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }

        Glide.with(this)
            .load(item.sellerPic)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(binding.picSeller)

        binding.msgToSellerBtn.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:${item.sellerTell}")
                putExtra("sms_body", "type your message")
            }
            startActivity(sendIntent)
        }

        binding.callToSellerBtn.setOnClickListener {
            val phone = item.sellerTell.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
    }
}
