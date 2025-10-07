package com.example.indiansignlanguage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.indiansignlanguage.components.AppBottomNavigation

@Composable
fun MainScreen(parentNavController: NavController) {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Home") {
                HomeScreen(navController = navController)
            }
            composable("modules") {
                Modules(navController = navController)
            }
            // Translator can be reached directly or with an optional query segment
            composable("translator") {
                TranslatorScreen(navController = navController)
            }
            composable("translator/{query}") {
                TranslatorScreen(navController = navController)
            }
            // Add missing learning destinations so navigation from Home/Modules works
            composable("commonwords") {
                CommonWordsScreen(navController = navController)
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
            composable("settings") {
                SettingsScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
        }
    }
}
