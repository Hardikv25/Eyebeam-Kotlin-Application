package com.example.Eyebeam

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    lateinit var btnCreate: Button
    lateinit var createEmail: EditText
    lateinit var loginLink: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        btnCreate = findViewById(R.id.btnCreateAccount)
        createEmail = findViewById(R.id.createEmail)
        loginLink = findViewById(R.id.tvLoginLink)

        val skip = findViewById<Button>(R.id.btn_skip)
        skip.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnCreate.setOnClickListener {
            val email = createEmail.text.toString().trim()

            // Check for empty email field
            if (email.isEmpty()) {
                createEmail.error = "Email is required"
                createEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate the email format
            if (!isValidEmail(email)) {
                createEmail.error = "Enter a valid email"
                createEmail.requestFocus()
                return@setOnClickListener
            }

            // Make API call to register user
            registerUser(email)
        }

        loginLink.setOnClickListener {
            // Redirect to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    private fun registerUser(email: String) {
        val request = RegisterRequest(email)
        apiService.registerUser(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please check your registered email and reset your password.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "You are already registered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
