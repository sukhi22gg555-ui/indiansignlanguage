package com.example.indiansignlanguage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Define colors for easy access and modification
val primaryBlue = Color(0xFF0D62FF)
val lightBlue = Color(0xFFE0EAFC)
val darkText = Color(0xFF1D2B4F)
val lightText = Color(0xFF75819D)
val backgroundColor = Color(0xFFF7F9FC)

@Composable
fun ProfileScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ISL Connect",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = darkText
            )

            Spacer(Modifier.height(32.dp))

            ProfileHeader()

            Spacer(Modifier.weight(1f))

            ProgressCircle(progress = 0.4f) // Represents 40%

            Spacer(Modifier.weight(1f))

            ActionButtons()
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Using Coil's AsyncImage to load a picture from a URL.
            // Replace the URL with a real one if you have it.
            AsyncImage(
                model = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=880&q=80",
                contentDescription = "Priya Sharma's profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(20.dp))

            Column {
                Text(
                    text = "Signs Learned: 152",
                    color = lightText,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Modules Completed: 8/20",
                    color = lightText,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Priya Sharama",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = darkText
        )
    }
}

@Composable
fun ProgressCircle(progress: Float) {
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
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = darkText
        )
    }
}


@Composable
fun ActionButtons() {
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
            ActionButton(icon = Icons.Default.EmojiEvents, text = "Achievements")
            ActionButton(icon = Icons.Default.Close, text = "Review Mistakes")
            ActionButton(icon = Icons.Default.Settings, text = "Settings")
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
    ProfileScreen()
}