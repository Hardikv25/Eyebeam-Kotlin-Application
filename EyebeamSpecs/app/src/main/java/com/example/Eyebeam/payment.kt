package com.example.Eyebeam

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Eyebeam.Helper.ManagmentCart
import com.example.Eyebeam.com.example.Eyebeam.Model.OrderModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class payment : AppCompatActivity(), PaymentResultListener {

    private lateinit var database: DatabaseReference
    private lateinit var managementCart: ManagmentCart

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("orders")
        managementCart = ManagmentCart(this) // Initialize managementCart

        val sharedPreferences = getSharedPreferences("OrderDetails", MODE_PRIVATE)
        val totalamt = sharedPreferences.getString("TotalAmount", "0")
        // Retrieve data from SharedPreferences
        val customerName = sharedPreferences.getString("CustomerName", "")
        val contactPhone = sharedPreferences.getString("CustomerPhone", "")
        val street = sharedPreferences.getString("Street", "")
        val city = sharedPreferences.getString("City", "")
        val pincode = sharedPreferences.getString("Pincode", "")
        val state = sharedPreferences.getString("State", "")
        val country = sharedPreferences.getString("Country", "")
        val itemTotal = sharedPreferences.getString("ItemTotal", "0")
        val tax = sharedPreferences.getString("Tax", "0")
        val deliveryCharge = sharedPreferences.getString("DeliveryCharge", "0")


        val totamount = findViewById<TextView>(R.id.totamt)
        val process = findViewById<Button>(R.id.process_payment)

        totamount.text = totalamt

        process.setOnClickListener {
            val amountText = totamount.text.toString().trim()  // Get the text from the TextView
            val numericAmount = amountText.replace(Regex("[^0-9]"), "")  // Remove non-numeric characters
            val amount = numericAmount.toIntOrNull()  // Convert the cleaned string to an integer safely
            if (amount != null && amount > 0) {
                startPayment(amount)  // Call startPayment if the amount is valid
            } else {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startPayment(amount: Int) {
        val checkout = Checkout()
        // Removing duplicate setKeyID since it is defined in the manifest
        // checkout.setKeyID("rzp_test_9krRg98kdpHw85")

        try {
            val options = JSONObject()
            options.put("name", "Razorpay Integration")
            options.put("description", "Learning tutorial")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "${(amount * 100)}")

            options.put("prefill.email", "virashardik2003@gmail.com")
            options.put("prefill.contact", "+919313420040")

            checkout.open(this, options)

        } catch (e: Exception) {
            Toast.makeText(this, "Error in Payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        managementCart.clearCart() // Call the method to clear the cart
        // Retrieve shared preferences data
        val userSharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val username = userSharedPreferences.getString("username", "0")
        val sharedPreferences = getSharedPreferences("OrderDetails", MODE_PRIVATE)
        val totalAmount = sharedPreferences.getString("TotalAmount", "0")
        val customerName = sharedPreferences.getString("CustomerName", "")
        val customerPhone = sharedPreferences.getString("CustomerPhone", "")
        val street = sharedPreferences.getString("Street", "")
        val city = sharedPreferences.getString("City", "")
        val pincode = sharedPreferences.getString("Pincode", "")
        val state = sharedPreferences.getString("State", "")
        val country = sharedPreferences.getString("Country", "")
        val productNames=sharedPreferences.getString("ProductNames","")
        val itemTotal = sharedPreferences.getString("ItemTotal", "0")
        val tax = sharedPreferences.getString("Tax", "0")
        val deliveryCharge = sharedPreferences.getString("DeliveryCharge", "0")
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


        // Create an Order object
        val order = OrderModel(
            username?:"",
            customerName ?: "",
            customerPhone ?: "",
            street ?: "",
            city ?: "",
            pincode ?: "",
            state ?: "",
            country ?: "",
            productNames?:"",
            itemTotal ?: "0",
            tax ?: "0",
            deliveryCharge ?: "0",
            totalAmount ?: "0",
            currentDate ?: ""
        )

        // Save order to Firebase
        saveOrderToFirebase(order)
        // Start the OrderDetailsActivity
        val intent = Intent(this@payment, OrderDetails::class.java)
        startActivity(intent)  // Start the OrderDetailsActivity
        finish()  // Finish the payment activity

    }

    private fun saveOrderToFirebase(order: OrderModel) {
        val orderId = database.push().key // Generate a unique ID for the order
        if (orderId != null) {
            database.child(orderId).setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    clearCartAfterPayment()
//                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Handle payment failure
    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show()
    }
}
