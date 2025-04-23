package com.example.Eyebeam.com.example.Eyebeam.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Eyebeam.Adapter.AdminCustomerAdapter
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCustomerModel
import com.example.Eyebeam.com.example.Eyebeam.Model.OrderModel
import com.google.firebase.database.DatabaseReference

class AdminOrderAdapter(
    private val orderList: ArrayList<OrderModel>,
    private val database: DatabaseReference,
    private val context: android.content.Context // Pass context to the adapter
) : RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custname: TextView = itemView.findViewById(R.id.customer_name)
        val custpno: TextView = itemView.findViewById(R.id.customer_phoneno)
        val custmail: TextView = itemView.findViewById(R.id.customer_mail)
        val productname: TextView = itemView.findViewById(R.id.product_name)
        val totalamount: TextView = itemView.findViewById(R.id.total_amount)
        val city: TextView = itemView.findViewById(R.id.address_city)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_vieworderdetails, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind data to the view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = orderList[position]

        holder.custname.text = item.customerName
        holder.custpno.text = item.customerPhone
        holder.custmail.text = item.username
        holder.productname.text = item.productNames
        holder.totalamount.text = item.totalAmount
        holder.city.text = item.city
    }


    // Return the size of the dataset
    override fun getItemCount(): Int {
        return orderList.size
    }
}
