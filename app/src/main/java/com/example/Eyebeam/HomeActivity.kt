package com.example.Eyebeam

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.Eyebeam.Adapter.BestSellerAdapter
import com.example.Eyebeam.Adapter.CategoryAdapter
import com.example.Eyebeam.Adapter.SliderAdapter
import com.example.Eyebeam.Model.SliderModel
import com.example.Eyebeam.ViewModel.MainViewModel
import com.example.Eyebeam.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    private val viewModel=MainViewModel()
    private lateinit var binding: ActivityHomeBinding
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

//    private lateinit var drawerLayout: DrawerLayout
private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBanners()
        initCategories()
        initBestSeller()
        bottomNavigation()

        //For display username on home page
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val displayName = username?.substringBefore("@") ?: ""

        binding.textuname.text = "Welcome, $displayName"

        // Access the NavigationView header to set the uname and umail TextView
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0) // Get the header view from navView

        // Update uname and umail TextViews in nav_header.xml
        val unameTextView = headerView.findViewById<TextView>(R.id.uname)
        val umailTextView = headerView.findViewById<TextView>(R.id.umail)

        unameTextView.text = displayName
        umailTextView.text = username

        drawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)

        // Set up the ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Enable the home button (hamburger icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle navigation item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Handle Home action
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_cart -> {
                    // Handle Profile action
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_myorder -> {
                    // Handle Settings action
                    val intent = Intent(this, OrderDetails::class.java)
                    startActivity(intent)
                }
                R.id.nav_explore -> {
                    // Handle Settings action
                    val intent = Intent(this, Explore::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    // Handle Logout action
                    val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                    val sessionId = sharedPreferences.getString("sessionId", "")
//                    Toast.makeText(this, sessionId.toString(), Toast.LENGTH_SHORT).show()
                    if (!sessionId.isNullOrEmpty()) {
                        logoutUser(sessionId)
                    } else {
                        Toast.makeText(this, "Session ID not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            drawerLayout.closeDrawers()
            true
        }


    }

    private fun logoutUser(sessionId:String){


        apiService.logoutUser(LogoutRequest(sessionId)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Clear session data
                    getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply()
                    Toast.makeText(this@HomeActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@HomeActivity, "Logout Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Override the function to open the drawer when the menu icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun bottomNavigation() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        binding.textCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        binding.imgexplore.setOnClickListener {
            startActivity(Intent(this,Explore::class.java))
        }
        binding.txtexplore.setOnClickListener {
            startActivity(Intent(this,Explore::class.java))
        }
        binding.seeallexplore.setOnClickListener {
            startActivity(Intent(this,Explore::class.java))
        }
        binding.btnWishlist.setOnClickListener {
            startActivity(Intent(this,WishlistActivity::class.java))
        }
        binding.textwishlist.setOnClickListener {
            startActivity(Intent(this,WishlistActivity::class.java))
        }
        binding.textwishlist.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }
        binding.btnOrder.setOnClickListener {
            startActivity(Intent(this,OrderDetails::class.java))
        }
        binding.textOrder.setOnClickListener {
            startActivity(Intent(this,OrderDetails::class.java))
        }
        binding.btnProfile.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }


        binding.textProfile.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }


    }

    private fun initBestSeller() {
        binding.progressBar4.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer { products ->
            // Shuffle the list to get a random order and take only the first 4 items
            val randomProducts = products.shuffled().take(4)

            binding.Product.layoutManager = GridLayoutManager(this, 2)
            binding.Product.adapter = BestSellerAdapter(randomProducts)
            binding.progressBar4.visibility = View.GONE
        })
        viewModel.loadBestSeller()
    }


    private fun initCategories()
    {
        binding.progressBar3.visibility=View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.category.layoutManager=LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
            binding.category.adapter=CategoryAdapter(it)
            binding.progressBar3.visibility=View.GONE
        })
        viewModel.loadCategory()
    }
    private fun initBanners() {
        binding.progressBar2.visibility=View.VISIBLE
        viewModel.banner.observe(this, Observer {
            banners(it)
            binding.progressBar2.visibility=View.GONE
        })
        viewModel.loadBanners()


    }
    private  fun banners(images:List<SliderModel>)
    {
        binding.ViewPager2.adapter=SliderAdapter(images,binding.ViewPager2)
        binding.ViewPager2.clipToPadding=false
        binding.ViewPager2.clipChildren=false
        binding.ViewPager2.offscreenPageLimit=3
        binding.ViewPager2.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.ViewPager2.setPageTransformer(compositePageTransformer)
        if(images.size>1)
        {
            binding.dotIndicator.visibility= View.VISIBLE
            binding.dotIndicator.attachTo(binding.ViewPager2)
        }
    }

}