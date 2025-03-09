package com.marindulja.therapism_mobile.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }
    var selectedRole by remember { mutableStateOf("User") } // Default role

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF1E1E2E))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLogin) "Login" else "Sign Up",
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(Color.White)
            )
            if (!isLogin) {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Radio buttons for role selection
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Role", color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = selectedRole == "User",
                        onClick = { selectedRole = "User" },
                        colors = RadioButtonDefaults.colors(Color.White)
                    )
                    Text("User", color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedRole == "Therapist",
                        onClick = { selectedRole = "Therapist" },
                        colors = RadioButtonDefaults.colors(Color.White)
                    )
                    Text("Therapist", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (isLogin) {
                        viewModel.authenticate(email.text, password.text, "", isLogin)
                    } else {
                        if (password.text == confirmPassword.text) {
                            viewModel.authenticate(email.text, password.text, selectedRole, isLogin)
                        } else {
                            viewModel.authResult = "Passwords do not match"
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isLogin) "Login" else "Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = viewModel.authResult, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login",
                color = Color.Cyan,
                modifier = Modifier.clickable { isLogin = !isLogin }
            )
        }
    }
}