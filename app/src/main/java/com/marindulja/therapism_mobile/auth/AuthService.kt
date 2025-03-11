package com.marindulja.therapism_mobile.auth

import com.marindulja.therapism_mobile.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API interface
interface AuthService {
    @POST("auth/authenticate")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}

data class AuthRequest(val email: String, val password: String)
data class RegisterRequest(val role: String, val email: String, val password: String)
data class AuthResponse(val user: User, val accessToken: String, val refreshToken: String)

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}
