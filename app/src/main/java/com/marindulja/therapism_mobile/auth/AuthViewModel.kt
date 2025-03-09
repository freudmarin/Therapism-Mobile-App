package com.marindulja.therapism_mobile.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var authResult by mutableStateOf("")

    fun authenticate(email: String, password: String, role: String, isLogin: Boolean) {
        viewModelScope.launch {
            try {
                 if (!isLogin) {
                    RetrofitClient.authService.register(RegisterRequest(email, password, role))
                } else {
                    RetrofitClient.authService.authenticate(AuthRequest(email, password))
                }
                authResult = "Successful authentication!"
            } catch (e: Exception) {
                authResult = "Unsuccessful authentication! : ${e.message}"
            }
        }
    }
}