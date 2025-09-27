package com.example.indiansignlanguage
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// --- Data class to hold number, icon, and video resource ID ---
// NOTE: You will need to add your video files (e.g., video_0.mp4, video_1.mp4)
// to your project's `res/raw` directory.
data class NumberItem(val number: Int, val icon: ImageVector, val videoResId: Int)

// --- Main Composable for the Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumbersScreen(navController: androidx.navigation.NavController) {
    // A list of numbers from 0 to 9 with placeholder icons and video IDs.
    // Replace '0' with your actual resource IDs, like `R.raw.video_0`.
    val numberItems = listOf(
        NumberItem(0, Icons.Default.Circle, 0),
        NumberItem(1, Icons.Default.LooksOne, 0),
        NumberItem(2, Icons.Default.LooksTwo, 0),
        NumberItem(3, Icons.Default.Looks3, 0),
        NumberItem(4, Icons.Default.Looks4, 0),
        NumberItem(5, Icons.Default.Looks5, 0),
        NumberItem(6, Icons.Default.Looks6, 0),
        NumberItem(7, Icons.Default.ThumbUp, 0),
        NumberItem(8, Icons.Default.WavingHand, 0),
        NumberItem(9, Icons.Default.FrontHand, 0)
    )

    // State to hold the currently selected number item for the dialog
    var selectedNumber by remember { mutableStateOf<NumberItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Numbers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(numberItems) { item ->
                NumberCard(item = item, onClick = { selectedNumber = item })
            }
        }

        selectedNumber?.let { item ->
            SignDetailDialog(item = item, onDismiss = { selectedNumber = null })
        }
    }
}

// --- Composable for a single number card ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberCard(item: NumberItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display only the number in the list item
            Text(
                text = item.number.toString(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF)
            )
        }
    }
}

// --- Composable for the sign detail dialog with Video Player ---
@Composable
fun SignDetailDialog(item: NumberItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Sign for: ${item.number}", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
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
                // Add the video player if a valid video resource is provided
                if (item.videoResId != 0) {
                    VideoPlayer(videoResId = item.videoResId)
                } else {
                    Text("No video available.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

// --- Composable for the Video Player ---
@Composable
fun VideoPlayer(videoResId: Int) {
    val context = LocalContext.current

    // 1. Create and remember an ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true // Autoplay
        }
    }

    // 2. A surface for the player to render the video
    // We use AndroidView to host a traditional Android View (PlayerView)
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f) // Standard 16:9 aspect ratio
    )

    // 3. Manage the player's lifecycle
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

// --- Preview for Android Studio ---
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun NumbersScreenPreview() {
    MaterialTheme {
        NumbersScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}

