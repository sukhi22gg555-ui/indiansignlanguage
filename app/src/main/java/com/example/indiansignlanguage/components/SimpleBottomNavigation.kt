package com.example.indiansignlanguage.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SimpleBottomNavigation(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Button
            SimpleNavButton(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = {
                    Log.d("SimpleNav", "Home clicked")
                    try {
                        navController.navigate("Home")
                        Log.d("SimpleNav", "Home navigation successful")
                    } catch (e: Exception) {
                        Log.e("SimpleNav", "Home navigation failed: ${e.message}")
                    }
                }
            )
            
            // Modules Button  
            SimpleNavButton(
                icon = Icons.Default.Book,
                label = "Modules",
                onClick = {
                    Log.d("SimpleNav", "Modules clicked")
                    try {
                        navController.navigate("modules")
                        Log.d("SimpleNav", "Modules navigation successful")
                    } catch (e: Exception) {
                        Log.e("SimpleNav", "Modules navigation failed: ${e.message}")
                    }
                }
            )
            
            // Translator Button
            SimpleNavButton(
                icon = Icons.Default.Translate,
                label = "Translator",
                onClick = {
                    Log.d("SimpleNav", "Translator clicked")
                    try {
                        navController.navigate("translator")
                        Log.d("SimpleNav", "Translator navigation successful")
                    } catch (e: Exception) {
                        Log.e("SimpleNav", "Translator navigation failed: ${e.message}")
                    }
                }
            )
            
            // Profile Button
            SimpleNavButton(
                icon = Icons.Default.Person,
                label = "Profile",
                onClick = {
                    Log.d("SimpleNav", "Profile clicked")
                    try {
                        navController.navigate("profile")
                        Log.d("SimpleNav", "Profile navigation successful")
                    } catch (e: Exception) {
                        Log.e("SimpleNav", "Profile navigation failed: ${e.message}")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(width = 70.dp, height = 60.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF007AFF).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF007AFF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF007AFF)
            )
        }
    }
}