package com.marindulja.therapism_mobile.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marindulja.therapism_mobile.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    var authState by mutableStateOf<AuthState>(AuthState.Idle)

    fun authenticate(email: String, password: String, role: String, isLogin: Boolean) {
        viewModelScope.launch {
            authState = AuthState.Loading
            try {
                val response = if (isLogin) {
                    RetrofitClient.createService(AuthService::class.java).authenticate(AuthRequest(email, password))
                } else {
                    RetrofitClient.createService(AuthService::class.java).register(RegisterRequest(role, email, password))
                }
                authState = AuthState.Success(
                    if (isLogin) "Login successful! Welcome back."
                    else "Registration successful! You can now log in."
                )
            } catch (e: HttpException) {
                // Internal error handling (optional)
                Log.e("AuthError", "HTTP exception occurred", e) // Logs full error for debugging

                // Generic messages for the user
                val errorMessage = when (e.code()) {
                    400 -> "Invalid input. Please check the details provided."
                    401 -> "Authentication failed. Please check your credentials."
                    404 -> "Service unavailable. Please try again later."
                    403 -> "Access denied. Please contact the administrator."
                    else -> "Something went wrong. Please try again."
                }

                authState = AuthState.Error(errorMessage) // Only user-friendly text is displayed
            } catch (e: Exception) {
                // Catch other exceptions and provide a generic fallback message for the user
                Log.e("AuthError", "Unexpected error occurred", e) // Logs the unexpected error
                authState = AuthState.Error("An unexpected error occurred. Please try again.")
            }

        }
    }
}
