package com.marindulja.therapism_mobile.auth

import com.marindulja.therapism_mobile.model.User
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API interface
interface AuthService {
    @POST("auth/authenticate")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/refresh") // Endpoint URL
    suspend fun refreshAccessToken(@Body request: RefreshTokenRequest): TokenResponse

}

data class AuthRequest(val email: String, val password: String)
data class RegisterRequest(val role: String, val email: String, val password: String)
data class AuthResponse(val user: User, val accessToken: String, val refreshToken: String)

// Request object for refreshing the token
data class RefreshTokenRequest(val token: String)

// Response object containing the new access token
data class TokenResponse(val token: String)