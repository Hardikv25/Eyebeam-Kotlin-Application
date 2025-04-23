package com.example.Eyebeam.com.example.Eyebeam.Adapter

import com.example.Eyebeam.com.example.Eyebeam.Model.AdminProductModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Eyebeam.R
import com.google.firebase.database.FirebaseDatabase

class AdminProductAdapter(val productList: ArrayList<AdminProductModel>): RecyclerView.Adapter<AdminProductAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val prodname: TextView = itemView.findViewById(R.id.prodname)
        val prodimage: ImageView = itemView.findViewById(R.id.prodimg)
        val prodprice: TextView = itemView.findViewById(R.id.prodprice)
        val deleteButton: ImageView = itemView.findViewById(R.id.proddel)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_viewproductdetails, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to the view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = productList[position]

        if (item.sellerPic.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.sellerPic) // Access first image safely
                .placeholder(R.drawable.user)
                .into(holder.prodimage)
        } else {
            holder.prodimage.setImageResource(R.drawable.user) // Fallback image if picUrl is empty
        }

        // Display the customer email
        holder.prodname.text = item.title
        holder.prodprice.text = item.price

        holder.deleteButton.setOnClickListener {
            val productId = item.title  // Assuming 'title' is unique
            FirebaseDatabase.getInstance().getReference("Items").child(productId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Product deleted", Toast.LENGTH_SHORT).show()
                    productList.removeAt(position)
                    notifyItemRemoved(position)
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
        }

    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return productList.size
    }
}
