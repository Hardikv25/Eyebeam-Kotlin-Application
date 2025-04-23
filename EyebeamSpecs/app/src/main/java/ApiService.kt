// ApiService.kt
package com.example.Eyebeam

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(val email: String)
data class LoginRequest(val email: String, val password: String)
data class LogoutRequest(val sessionId: String)


// API service interface
interface ApiService {
    @POST("register")  // Replace with your actual API endpoint for registration
    fun registerUser(@Body request: RegisterRequest): Call<Void>

    @POST("login")  // Replace with your actual API endpoint for login
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("logout")
    fun logoutUser(@Body request: LogoutRequest): Call<Void>
}
data class LoginResponse(
    val sessionId: String,
    val username: String,
    val uid: String
)
