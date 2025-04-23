package com.example.Eyebeam.com.example.Eyebeam.Model

data class OrderModel(
    val username:String="",
    val customerName: String = "",
    val customerPhone: String = "",
    val street: String = "",
    val city: String = "",
    val pincode: String = "",
    val state: String = "",
    val country: String = "",
    val productNames:String="",
    val itemTotal: String = "0",
    val tax: String = "0",
    val deliveryCharge: String = "0",
    val totalAmount: String = "0",
    val currentDate: String = ""
)
