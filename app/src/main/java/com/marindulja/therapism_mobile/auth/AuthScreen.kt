package com.marindulja.therapism_mobile.auth

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightBackground = Color(0xFFF4F7F6) // Soft mint green/lavender
private val PrimaryButtonColor = Color(0xFF5DB075) // Calming teal green
private val SelectedColor = Color(0xFF648FFF) // Soft blue for selected items
private val UnselectedColor = Color(0xFFB0BEC5) // Light gray for unselected items
private val TextColor = Color(0xFF333333) // Dark gray
private val TextFieldBackground = Color(0xFFF0F0F0) // Light gray for text fields

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }
    var selectedRole by remember { mutableStateOf("User") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.authState) {
        when (val state = viewModel.authState) {
            is AuthState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                navigateToHome() // Navigate on success
            }

            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG)
                    .show() // Show toast on error
            }

            is AuthState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            }

            is AuthState.Idle -> {
                // Optionally handle idle state or do nothing
            }
        }
    }

    fun validateInputs(): Boolean {
        var isValid = true

        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            isValid = false
            "Invalid email format"
        } else ""

        if (!isLogin) {
            passwordError = if (password.text.length < 6) {
                isValid = false
                "Password must be at least 6 characters"
            } else ""

            confirmPasswordError = if (password.text != confirmPassword.text) {
                isValid = false
                "Passwords do not match"
            } else ""
        }

        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isLogin) "Welcome Back" else "Create an Account",
                fontSize = 32.sp,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = { Text("Email") },
                isError = emailError.isNotEmpty(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.White,
                    focusedIndicatorColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        TextFieldBackground,
                        shape = RoundedCornerShape(8.dp)
                    ) // Apply background
                    .padding(4.dp), // Optional padding inside the background

            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text("Password") },
                isError = passwordError.isNotEmpty(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.White,
                    focusedIndicatorColor = Color.LightGray,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        TextFieldBackground,
                        shape = RoundedCornerShape(8.dp)
                    ) // Apply background
                    .padding(4.dp), // Optional padding inside the background

            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red, fontSize = 12.sp)
            }

            if (!isLogin) {
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = ""
                    },
                    label = { Text("Confirm Password") },
                    isError = confirmPasswordError.isNotEmpty(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.LightGray,
                        cursorColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            TextFieldBackground,
                            shape = RoundedCornerShape(8.dp)
                        ) // Apply background
                        .padding(4.dp), // Optional padding inside the background


                )
                if (confirmPasswordError.isNotEmpty()) {
                    Text(confirmPasswordError, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Ensure the row takes the full width
                        .padding(vertical = 8.dp), // Add vertical padding for the row
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically // Align items vertically at the center
                ) {
                    // RoleOption for User
                    RoleOption(
                        label = "User",
                        selected = selectedRole == "User"
                    ) { selectedRole = "User" }

                    Spacer(modifier = Modifier.width(16.dp)) // Adds spacing between buttons

                    // RoleOption for Therapist
                    RoleOption(
                        label = "Therapist",
                        selected = selectedRole == "Therapist"
                    ) { selectedRole = "Therapist" }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validateInputs()) {
                       viewModel.authenticate(
                            email.text,
                            password.text,
                            selectedRole,
                            isLogin
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryButtonColor)
            ) {
                Text(
                    text = if (isLogin) "Login" else "Sign Up",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login",
                color = Color.DarkGray,
                modifier = Modifier.clickable { isLogin = !isLogin },
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RoleOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.Start, // Optional, aligns items to start horizontally
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp) // Adds vertical padding for better spacing
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = SelectedColor,
                unselectedColor = UnselectedColor
            )
        )
        Text(
            text = label,
            color = if (selected) SelectedColor else TextColor, // Adjust color for unselected items
            modifier = Modifier
                .padding(start = 8.dp) // Space between the RadioButton and Text
                .align(Alignment.CenterVertically) // Ensures text stays aligned with the radio button
        )
    }
}

