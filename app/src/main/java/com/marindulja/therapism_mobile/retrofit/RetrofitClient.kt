package com.marindulja.therapism_mobile.retrofit

import android.content.Context
import androidx.core.content.edit
import com.marindulja.therapism_mobile.auth.AuthService
import com.marindulja.therapism_mobile.auth.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/" // Replace with your backend URL
    private lateinit var retrofit: Retrofit

    // Initialization method to configure the client
    fun initialize(context: Context, refreshToken: String) {
        val okHttpClient = createOkHttpClient(context, refreshToken)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON serialization/deserialization
            .build()
    }

    // Public method to provide APIs
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    // Configures the OkHttpClient with an Interceptor for token management
    private fun createOkHttpClient(context: Context, refreshToken: String): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Add interceptor to attach the access token to all requests
        clientBuilder.addInterceptor { chain ->
            val originalRequest = chain.request()

            // Skip Authorization for login and registration
            if (originalRequest.url().encodedPath().contains("auth/authenticate") ||
                originalRequest.url().encodedPath().contains("auth/register")
            ) {
                return@addInterceptor chain.proceed(originalRequest)
            }

            val accessToken = retrieveAccessToken(context)

            // Attach access token to the request
            val request = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val response = chain.proceed(request)

            // Refresh the token if it's expired and retry
            if (response.code() == 401) {
                response.close() // Close the previous response
                val newToken = runBlocking { refreshAccessToken(context, refreshToken) }
                saveAccessToken(context, newToken)

                // Retry with the new token
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()

                return@addInterceptor chain.proceed(newRequest)
            }

            return@addInterceptor response
        }

        return clientBuilder.build()
    }

    // Simulates retrieving the stored access token (replace with actual shared preferences or encrypted store logic)
    private fun retrieveAccessToken(context: Context): String {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
    }

    // Simulates saving the access token (replace with actual logic)
    private fun saveAccessToken(context: Context, accessToken: String) {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .edit() {
                putString("ACCESS_TOKEN", accessToken)
            }
    }

    // Refresh the token by calling the "refreshAccessToken" API
    suspend fun refreshAccessToken(context: Context, refreshToken: String): String {
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        val authService = retrofit.create(AuthService::class.java)

        return authService.refreshAccessToken(refreshTokenRequest).token
    }
}
