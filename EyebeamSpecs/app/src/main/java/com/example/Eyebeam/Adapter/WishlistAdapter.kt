package com.example.Eyebeam.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Eyebeam.Model.WishlistModel
import com.example.Eyebeam.R

class WishlistAdapter(
    private val items: MutableList<WishlistModel>,
    private val onRemove: (WishlistModel) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.titleTxt)
        private val price: TextView = itemView.findViewById(R.id.priceTxt)
        private val image: ImageView = itemView.findViewById(R.id.productImage)
        private val removeButton: ImageView = itemView.findViewById(R.id.removeIcon)

        fun bind(item: WishlistModel) {
            title.text = item.title
            price.text = "₹${item.price}"
            Glide.with(itemView.context).load(item.picUrl).into(image)
            removeButton.setOnClickListener {
                onRemove(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
