package com.marindulja.therapism_mobile.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
                    RetrofitClient.authService.authenticate(AuthRequest(email, password))
                } else {
                    RetrofitClient.authService.register(RegisterRequest(role, email, password))
                }
                authState = AuthState.Success(
                    if (isLogin) "Login successful! Welcome back."
                    else "Registration successful! You can now log in."
                )
            } catch (e: HttpException) {
                authState = AuthState.Error(when (e.code()) {
                    400 -> "Bad request. Please check your input."
                    401 -> "Unauthorized. Please check your email and password."
                    404 -> "Server not found. Try again later."
                    else -> "An error occurred (${e.code()}). Please try again."
                })
            } catch (e: IOException) {
                authState = AuthState.Error("Please check your internet connection.")
            } catch (e: Exception) {
                authState = AuthState.Error("Something went wrong. Please try again.")
            }
        }
    }
}
