package com.marindulja.therapism_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marindulja.therapism_mobile.auth.AuthScreen
import com.marindulja.therapism_mobile.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ensure this method is properly defined elsewhere in the project.
        setContent {
            AppNavigation()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewAuthScreen() {
    AuthScreen(
        viewModel = AuthViewModel(),
        navigateToHome = {} // Provide an empty lambda since this is just a preview.
    )
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome to the Home Screen!",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Initialize the navigation controller.

    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            // Provide the `AuthViewModel` instance and authorize navigation.
            val viewModel = remember { AuthViewModel() } // Replace with a proper DI solution if needed.
            AuthScreen(
                viewModel = viewModel,
                navigateToHome = {
                    // Navigate to the HomeScreen after successful login/registration and clear backstack.
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}