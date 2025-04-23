package com.example.Eyebeam.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCustomerModel
import com.google.firebase.database.DatabaseReference

class AdminCustomerAdapter(
    private val usersList: ArrayList<AdminCustomerModel>,
    private val database: DatabaseReference,
    private val context: android.content.Context // Pass context to the adapter
) : RecyclerView.Adapter<AdminCustomerAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custemail: TextView = itemView.findViewById(R.id.cust_email)   // Customer email
        val custNumber: TextView = itemView.findViewById(R.id.cust_num) // Customer number
        val custDel: ImageView = itemView.findViewById(R.id.custdel) // Delete button
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_viewcustomerdetails, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to the view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = usersList[position]

        // Display the incremented number (position + 1)
        holder.custNumber.text = (position + 1).toString()

        // Display the customer email
        holder.custemail.text = item.email // Assuming CustomerModel has an 'email' field

        // Set a click listener for the delete button
        holder.custDel.setOnClickListener {
            deleteCustomer(item.email, position)
        }
    }

    // Function to delete a customer
    private fun deleteCustomer(email: String, position: Int) {
        // Find the customer in the database using their email
        database.orderByChild("email").equalTo(email).get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.exists()) {
                for (snapshot in task.result.children) {
                    // Delete the customer from the database
                    snapshot.ref.removeValue().addOnCompleteListener { deleteTask ->
                        if (deleteTask.isSuccessful) {
                            // Remove from the local list and notify adapter
                            usersList.removeAt(position)
                            notifyItemRemoved(position)
                            // Show success Toast
                            Toast.makeText(context, "Customer deleted successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Show error Toast
                            Toast.makeText(context, "Failed to delete customer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Show message if no customer was found
                Toast.makeText(context, "No customer found with that email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return usersList.size
    }
}
