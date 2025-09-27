package com.example.indiansignlanguage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Data class to hold the greeting text and icon ---
data class GreetingItem(val text: String, val icon: ImageVector)

// --- Main Composable for the Greetings Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingsScreen(navController: androidx.navigation.NavController) {
    // A list of greetings with placeholder icons.
    val greetingItems = listOf(
        GreetingItem("Hello", Icons.Default.WavingHand),
        GreetingItem("Good Morning", Icons.Default.LightMode),
        GreetingItem("Thank You", Icons.Default.Favorite),
        GreetingItem("Please", Icons.Default.ThumbUp),
        GreetingItem("Sorry", Icons.Default.SentimentVeryDissatisfied),
        GreetingItem("How are you?", Icons.Default.QuestionAnswer)
    )

    // State to manage the dialog visibility and content
    var selectedGreeting by remember { mutableStateOf<GreetingItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Greetings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF007AFF))
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(greetingItems) { item ->
                GreetingCard(item = item, onClick = { selectedGreeting = item })
            }
        }

        // Show a dialog when a greeting is selected
        selectedGreeting?.let { item ->
            GreetingDetailDialog(item = item, onDismiss = { selectedGreeting = null })
        }
    }
}

// --- Composable for a single greeting card in the list ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingCard(item: GreetingItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Box with a border for the icon, matching the reference image
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(BorderStroke(2.dp, Color(0xFF007AFF)), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

// --- Composable for the greeting detail dialog ---
@Composable
fun GreetingDetailDialog(item: GreetingItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = item.text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color(0xFF007AFF),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // You could add a video player here in the future
                Text("Video player would be shown here.", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}


// --- Preview for Android Studio ---
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun GreetingsScreenPreview() {
    MaterialTheme {
        GreetingsScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}
