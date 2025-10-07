package com.example.indiansignlanguage

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Settings options
    val settingsItems = listOf(
        SettingsItem(
            icon = Icons.Default.Person,
            title = "Account Settings",
            subtitle = "Manage your profile and account",
            onClick = { /* Navigate to account settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Configure app notifications",
            onClick = { /* Navigate to notification settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.Language,
            title = "Language",
            subtitle = "Change app language",
            onClick = { /* Navigate to language settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.Palette,
            title = "Theme",
            subtitle = "Light or dark mode",
            onClick = { /* Navigate to theme settings */ }
        ),
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.VolumeUp,
            title = "Audio Settings",
            subtitle = "Sound and vibration preferences",
            onClick = { /* Navigate to audio settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.Speed,
            title = "Animation Speed",
            subtitle = "Adjust sign animation speed",
            onClick = { /* Navigate to animation settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.School,
            title = "Learning Preferences",
            subtitle = "Customize your learning experience",
            onClick = { /* Navigate to learning settings */ }
        ),
        SettingsItem(
            icon = Icons.Default.Info,
            title = "About",
            subtitle = "App version and information",
            onClick = { /* Navigate to about screen */ }
        ),
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.Help,
            title = "Help & Support",
            subtitle = "Get help and contact support",
            onClick = { /* Navigate to help screen */ }
        ),
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Logout",
            subtitle = "Sign out of your account",
            isDestructive = true,
            onClick = { showLogoutDialog = true }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Settings", 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A90E2)
                )
            )
        },
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->
        
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                // User info section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar placeholder
                        Surface(
                            modifier = Modifier.size(60.dp),
                            shape = RoundedCornerShape(30.dp),
                            color = Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Avatar",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = auth.currentUser?.email ?: "Guest User",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF333333)
                            )
                            Text(
                                text = if (auth.currentUser != null) "Signed in" else "Not signed in",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "App Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }
            
            items(settingsItems.size) { index ->
                SettingsItemCard(
                    item = settingsItems[index],
                    isLast = index == settingsItems.size - 1
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
                
                // App version info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Indian Sign Language App",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = "Version 1.0.0",
                            fontSize = 12.sp,
                            color = Color(0xFF888888)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Learn Indian Sign Language with interactive lessons and practice modules.",
                            fontSize = 12.sp,
                            color = Color(0xFF888888),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Are you sure you want to logout? You'll need to sign in again to access your progress.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                auth.signOut()
                                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                                
                                // Navigate to login screen and clear back stack
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error logging out: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                        showLogoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE57373)
                    )
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class SettingsItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItemCard(
    item: SettingsItem,
    isLast: Boolean = false
) {
    Card(
        onClick = item.onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(20.dp),
                color = if (item.isDestructive) 
                    Color(0xFFFFEBEE) 
                else 
                    Color(0xFF4CAF50).copy(alpha = 0.1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (item.isDestructive) 
                            Color(0xFFE57373) 
                        else 
                            Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (item.isDestructive) 
                        Color(0xFFE57373) 
                    else 
                        Color(0xFF333333)
                )
                Text(
                    text = item.subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            
            // Arrow icon
            if (!item.isDestructive) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color(0xFFBBBBBB),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}