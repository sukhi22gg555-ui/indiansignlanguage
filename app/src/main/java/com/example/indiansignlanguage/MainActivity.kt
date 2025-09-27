package com.example.indiansignlanguage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.indiansignlanguage.ui.theme.IndianSignLanguageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndianSignLanguageTheme {
                // The NavController is the central controller for navigation
                val navController = rememberNavController()

                // NavHost is the container that displays the screen for the current route
                NavHost(navController = navController, startDestination = "login") {

                    // Defines the screen for the "login" route
                    composable("login") {
                        LoginScreen(navController = navController)
                    }

                    // Defines the screen for the "signup" route
                    composable("signup") {
                        SignUpScreen(navController = navController)
                    }
                }
            }
        }
    }
}