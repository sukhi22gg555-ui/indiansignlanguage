package com.example.indiansignlanguage

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.indiansignlanguage.data.UserProfile
import com.example.indiansignlanguage.utils.FirebaseUtils
import kotlinx.coroutines.launch

// Define colors for easy access and modification
val primaryBlue = Color(0xFF0D62FF)
val lightBlue = Color(0xFFE0EAFC)
val darkText = Color(0xFF1D2B4F)
val lightText = Color(0xFF75819D)
val backgroundColor = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    rootNavController: NavController? = null
) {
    // Log when ProfileScreen loads
    LaunchedEffect(Unit) {
        Log.d("Navigation", "ProfileScreen composable loaded and displayed")
    }

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Load user profile when screen opens
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            FirebaseUtils.getUserProfile().fold(
                onSuccess = { profile ->
                    userProfile = profile
                    isLoading = false
                },
                onFailure = {
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        fontWeight = FontWeight.SemiBold,
                        color = darkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = darkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                ProfileHeader(userProfile)

                Spacer(Modifier.height(32.dp))

                ProgressCircle(userProfile = userProfile)

                Spacer(Modifier.height(32.dp))

                ActionButtons(navController, authViewModel, rootNavController)

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(userProfile: UserProfile?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile picture placeholder
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                color = lightBlue
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = primaryBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(Modifier.width(20.dp))

            Column {
                Text(
                    text = "Lessons Completed: ${userProfile?.totalLessonsCompleted ?: 0}",
                    color = lightText,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Total Score: ${userProfile?.totalScore ?: 0}",
                    color = lightText,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Current Level: ${userProfile?.currentLevel ?: 1}",
                    color = lightText,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = userProfile?.fullName ?: "Loading...",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = darkText
        )
    }
}

@Composable
fun ProgressCircle(userProfile: UserProfile?) {
    // Calculate progress based on lessons completed (assume 100 lessons total)
    val totalLessonsTarget = 100
    val completedLessons = userProfile?.totalLessonsCompleted ?: 0
    val progress = (completedLessons.toFloat() / totalLessonsTarget).coerceAtMost(1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp)
    ) {
        // Background track
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = lightBlue,
                startAngle = -225f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 25f, cap = StrokeCap.Round)
            )
        }
        // Foreground progress indicator
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = primaryBlue,
                startAngle = -225f,
                sweepAngle = 270f * progress,
                useCenter = false,
                style = Stroke(width = 25f, cap = StrokeCap.Round)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkText
            )
            Text(
                text = "$completedLessons/$totalLessonsTarget lessons",
                fontSize = 14.sp,
                color = lightText
            )
        }
    }
}


@Composable
fun ActionButtons(navController: NavController, authViewModel: AuthViewModel, rootNavController: NavController?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(icon = Icons.Default.EmojiEvents, text = "Achievements", onClick = {})
            ActionButton(icon = Icons.Default.Close, text = "Review Mistakes", onClick = {})
            ActionButton(icon = Icons.Default.Settings, text = "Settings", onClick = {})
            ActionButton(icon = Icons.AutoMirrored.Filled.Logout, text = "Logout", onClick = {
                authViewModel.logout()
                val controller = rootNavController ?: navController
                controller.navigate("login") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (text == "Achievements") primaryBlue else lightText,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = text,
            color = if (text == "Achievements") primaryBlue else lightText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}