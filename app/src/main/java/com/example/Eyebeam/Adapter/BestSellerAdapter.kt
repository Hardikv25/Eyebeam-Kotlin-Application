package com.example.Eyebeam.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.Eyebeam.DetailActivity
import com.example.Eyebeam.Model.ItemsModel
import com.example.Eyebeam.databinding.ViewBestSellerBinding

class BestSellerAdapter(val items: List<ItemsModel>): RecyclerView.Adapter<BestSellerAdapter.Viewholder>() {
    private var context: Context? = null

    class Viewholder(val binding: ViewBestSellerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): Viewholder {
        context=parent.context
        val binding=ViewBestSellerBinding.inflate(
            LayoutInflater.from(context),parent,false
        )
        return Viewholder(binding)
    }


    override fun onBindViewHolder(holder: Viewholder, position: Int) {
       holder.binding.textView14.text=items[position].title
        holder.binding.textView12.text="₹"+items[position].price.toString()
        holder.binding.textView13.text=items[position].rating.toString()
        val requestOptions=RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl[0])
            .apply(requestOptions)
            .into(holder.binding.picbestseller)

        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("object",items[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}