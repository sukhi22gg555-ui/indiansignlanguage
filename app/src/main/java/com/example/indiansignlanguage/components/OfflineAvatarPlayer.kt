package com.example.indiansignlanguage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import com.example.indiansignlanguage.utils.SiGMLPlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OfflineAvatarPlayer(
    text: String,
    modifier: Modifier = Modifier,
    sigmlPlayerManager: SiGMLPlayerManager,
    onSignComplete: () -> Unit = {}
) {
    var availableSigns by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentSignIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentSiGML by remember { mutableStateOf<String?>(null) }
    var sigmlDescription by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Load available signs when text changes
    LaunchedEffect(text) {
        availableSigns = sigmlPlayerManager.findAvailableSignsInText(text)
        currentSignIndex = 0
        if (availableSigns.isNotEmpty()) {
            isPlaying = true
            playSignSequence(
                availableSigns,
                sigmlPlayerManager,
                onSignUpdate = { index, sigml, description ->
                    currentSignIndex = index
                    currentSiGML = sigml
                    sigmlDescription = description
                },
                onComplete = {
                    isPlaying = false
                    onSignComplete()
                }
            )
        }
    }
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Display Area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = Color(0xFFE8F5E8),
                border = BorderStroke(2.dp, Color(0xFF4CAF50))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (availableSigns.isEmpty()) {
                        // No offline signs available
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "No Signs",
                                modifier = Modifier.size(48.dp),
                                tint = Color(0xFF888888)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No offline signs available",
                                color = Color(0xFF666666),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "for \"$text\"",
                                color = Color(0xFF888888),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Show current sign being played
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Animated avatar representation
                            SiGMLAvatarDisplay(
                                currentSiGML = currentSiGML,
                                isPlaying = isPlaying,
                                currentWord = if (currentSignIndex < availableSigns.size) 
                                    availableSigns[currentSignIndex] else "",
                                modifier = Modifier.size(120.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Current sign info
                            if (currentSignIndex < availableSigns.size) {
                                Text(
                                    text = "Signing: ${availableSigns[currentSignIndex]}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "${currentSignIndex + 1} of ${availableSigns.size}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Available Signs List
            if (availableSigns.isNotEmpty()) {
                Text(
                    text = "Available Signs in \"$text\":",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(availableSigns.mapIndexed { index, sign -> Pair(index, sign) }) { (index, sign) ->
                        SignItem(
                            sign = sign,
                            isCurrentlyPlaying = index == currentSignIndex && isPlaying,
                            onSignClick = {
                                coroutineScope.launch {
                                    playIndividualSign(sign, sigmlPlayerManager) { sigml, description ->
                                        currentSiGML = sigml
                                        sigmlDescription = description
                                    }
                                }
                            }
                        )
                    }
                }
            }
            
            // Control Buttons
            if (availableSigns.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                isPlaying = true
                                playSignSequence(
                                    availableSigns,
                                    sigmlPlayerManager,
                                    onSignUpdate = { index, sigml, description ->
                                        currentSignIndex = index
                                        currentSiGML = sigml
                                        sigmlDescription = description
                                    },
                                    onComplete = {
                                        isPlaying = false
                                        onSignComplete()
                                    }
                                )
                            }
                        },
                        enabled = !isPlaying,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Replay",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Replay All", fontSize = 12.sp)
                    }
                    
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val allSigns = sigmlPlayerManager.getAvailableOfflineSigns()
                                // Show available signs info
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${availableSigns.size} Signs", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SiGMLAvatarDisplay(
    currentSiGML: String?,
    isPlaying: Boolean,
    currentWord: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Try to load a human avatar drawable named "human_avatar" from res/drawable.
    val humanAvatarResId = remember {
        context.resources.getIdentifier("human_avatar", "drawable", context.packageName)
    }

    // Subtle breathing animation for the avatar image when playing
    val infinite = rememberInfiniteTransition(label = "avatar-breathe")
    val scale by infinite.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isPlaying) 1.04f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isPlaying) 1200 else 1, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    // Emoji fallback sequence if no drawable exists
    val animatedEmojis = remember { listOf("🤟", "👋", "🙏", "👍", "✋", "👌", "🤲", "👐", "🖐️", "✌️") }
    var currentEmojiIndex by remember { mutableStateOf(0) }

    LaunchedEffect(isPlaying, currentSiGML) {
        if (humanAvatarResId == 0 && isPlaying && currentSiGML != null) {
            // Emoji fallback animation
            while (isPlaying) {
                delay(500)
                currentEmojiIndex = (currentEmojiIndex + 1) % animatedEmojis.size
            }
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(60.dp),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
        border = BorderStroke(2.dp, Color(0xFF4CAF50))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (humanAvatarResId != 0) {
                // Render human avatar image with subtle breathing animation
                Image(
                    painter = painterResource(id = humanAvatarResId),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                // Fallback: emoji glyphs
                Text(
                    text = if (isPlaying) animatedEmojis[currentEmojiIndex] else "🤟",
                    fontSize = 48.sp
                )
            }

            if (isPlaying) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = currentWord,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun SignItem(
    sign: String,
    isCurrentlyPlaying: Boolean,
    onSignClick: () -> Unit
) {
    Card(
        onClick = onSignClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentlyPlaying) 
                Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFFF5F5F5)
        ),
        border = if (isCurrentlyPlaying) BorderStroke(1.dp, Color(0xFF4CAF50)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isCurrentlyPlaying) Icons.Default.PlayArrow else Icons.Default.Gesture,
                contentDescription = "Sign",
                modifier = Modifier.size(16.dp),
                tint = if (isCurrentlyPlaying) Color(0xFF4CAF50) else Color(0xFF666666)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = sign,
                fontSize = 14.sp,
                color = if (isCurrentlyPlaying) Color(0xFF4CAF50) else Color(0xFF333333),
                fontWeight = if (isCurrentlyPlaying) FontWeight.Medium else FontWeight.Normal
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isCurrentlyPlaying) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

// Helper functions
private suspend fun playSignSequence(
    signs: List<String>,
    sigmlPlayerManager: SiGMLPlayerManager,
    onSignUpdate: (Int, String?, String) -> Unit,
    onComplete: () -> Unit
) {
    for (index in signs.indices) {
        val sign = signs[index]
        val sigmlContent = sigmlPlayerManager.loadSiGMLFromAssets(sign)
        val description = parseSiGMLDescription(sigmlContent)
        
        onSignUpdate(index, sigmlContent, description)
        
        // Simulate sign duration based on complexity
        val duration = calculateSignDuration(sigmlContent)
        delay(duration)
    }
    onComplete()
}

private suspend fun playIndividualSign(
    sign: String,
    sigmlPlayerManager: SiGMLPlayerManager,
    onSignUpdate: (String?, String) -> Unit
) {
    val sigmlContent = sigmlPlayerManager.loadSiGMLFromAssets(sign)
    val description = parseSiGMLDescription(sigmlContent)
    onSignUpdate(sigmlContent, description)
    
    val duration = calculateSignDuration(sigmlContent)
    delay(duration)
}

private fun parseSiGMLDescription(sigmlContent: String?): String {
    if (sigmlContent == null) return "No SiGML data available"
    
    // Basic SiGML parsing - look for common elements
    return when {
        sigmlContent.contains("hamflathand") -> "Flat hand gesture"
        sigmlContent.contains("hamfist") -> "Closed fist gesture"
        sigmlContent.contains("hampinch") -> "Pinch gesture"
        sigmlContent.contains("hammove") -> "Movement gesture"
        sigmlContent.contains("hamtouch") -> "Touch gesture"
        else -> "Sign language gesture"
    }
}

private fun calculateSignDuration(sigmlContent: String?): Long {
    if (sigmlContent == null) return 1000L
    
    // Calculate duration based on SiGML complexity
    val baseTime = 1000L
    val movements = sigmlContent.count { it.toString().contains("move", ignoreCase = true) }
    val gestures = sigmlContent.count { it.toString().contains("ham", ignoreCase = true) }
    
    return baseTime + (movements * 300L) + (gestures * 100L)
}