package com.example.Eyebeam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Eyebeam.Admin.Dashboard
import com.example.Eyebeam.databinding.ActivityLoginBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.loginEmail.error = "Email is required"
                binding.loginEmail.requestFocus()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                binding.loginEmail.error = "Enter a valid email address"
                binding.loginEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.loginPassword.error = "Password is required"
                binding.loginPassword.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.signupRedirectText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to make API call to login
    private fun loginUser(email: String, password: String) {

        // Check for admin credentials
        if (email == "admin@gmail.com" && password == "admin@123") {
            // Directly redirect to DashboardActivity
            Toast.makeText(this@LoginActivity, "Admin Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, Dashboard::class.java))
            finish()
            return
        }
        // Proceed with API call if not admin
        val request = LoginRequest(email, password)
        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()
                val sessionId = loginResponse?.sessionId
                val username = loginResponse?.username
                val uid=loginResponse?.uid
                if (sessionId != null && username != null) {
                    // Save session ID and username in shared preferences
                    val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                    sharedPreferences.edit().apply {
                        putString("sessionId", sessionId)
                        putString("username", username)
                        putString("uid", uid)
                        apply()
                    }
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    // Redirect to home or main activity
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java).putExtra("email", email))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
