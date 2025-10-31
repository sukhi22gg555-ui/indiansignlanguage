package com.example.indiansignlanguage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.indiansignlanguage.ui.theme.IndianSignLanguageTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IndianSignLanguageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen {
                            showSplash = false
                        }
                    } else {
                        val navController = rememberNavController()

                        val isLoggedIn = Firebase.auth.currentUser != null
                        NavHost(navController = navController, startDestination = if (isLoggedIn) "main" else "login") {
                            composable("login") {
                                LoginScreen(navController = navController)
                            }
                            composable("signup") {
                                SimpleSignUpScreen(navController = navController)
                            }
                            composable("main") {
                                MainScreen(parentNavController = navController)
                            }
                            composable("numbers") {
                                NumbersScreen(navController = navController)
                            }
                            composable("greetings") {
                                GreetingsScreen(navController = navController)
                            }
                            composable("alphabets") {
                                AlphabetsScreen(navController = navController)
                            }
                            composable("commonwords") {
                                CommonWordsScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
