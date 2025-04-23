package com.example.Eyebeam

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.Eyebeam.Adapter.BestSellerAdapter
import com.example.Eyebeam.ViewModel.MainViewModel
import com.example.Eyebeam.databinding.ActivityExploreBinding

class Explore : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityExploreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding before accessing it
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backexplrebtn.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

        // Call initBestSeller after binding has been initialized
        initBestSeller()
    }

    private fun initBestSeller() {
        binding.progressBarExplore.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this) { products ->

            binding.recyclerViewExplore.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewExplore.adapter = BestSellerAdapter(products)
            binding.progressBarExplore.visibility = View.GONE
        }
        viewModel.loadBestSeller()
    }

}
