package com.example.Eyebeam.com.example.Eyebeam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.Eyebeam.Admin.ManageCategory
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCategoryModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminCategoryAdapter(
    private val categoryList: ArrayList<AdminCategoryModel>,
    database: DatabaseReference,
    manageCategory: ManageCategory
) : RecyclerView.Adapter<AdminCategoryAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catename: TextView = itemView.findViewById(R.id.catetitle)
        val cateimage: ImageView = itemView.findViewById(R.id.cateimg)
        val deleteButton: ImageView = itemView.findViewById(R.id.catedelete)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_viewcategorydetails, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to the view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = categoryList[position]

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .placeholder(R.drawable.user)
            .into(holder.cateimage)

        holder.catename.text = item.title

        // Set up delete button click listener
        holder.deleteButton.setOnClickListener {
            deleteCategory(holder.itemView.context, item.title, position)
        }
    }

    // Delete the category from Firebase and update RecyclerView
    private fun deleteCategory(context: Context, title: String, position: Int) {
        // Find the category in the database using its title
        val databaseRef = FirebaseDatabase.getInstance().getReference("Category")
        databaseRef.orderByChild("title").equalTo(title).get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.exists()) {
                for (snapshot in task.result.children) {
                    // Delete the category from the database
                    snapshot.ref.removeValue().addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            // Remove from the local list
                            categoryList.removeAt(position)

                            // Notify adapter of item removal and data set change
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, categoryList.size)

                            // Show success Toast
                            Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Show error Toast
                            Toast.makeText(context, "Failed to delete category", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Show message if no category was found
                Toast.makeText(context, "No category found with that title", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Return the size of the dataset
    override fun getItemCount(): Int {
        return categoryList.size
    }
}
