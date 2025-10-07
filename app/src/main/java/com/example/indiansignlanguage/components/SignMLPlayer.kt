package com.example.indiansignlanguage.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * SignML Player component that displays sign language animations from .sigml files
 */
@Composable
fun SignMLPlayer(
    signName: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showControls: Boolean = true,
    onPlayComplete: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var sigmlContent by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentFrame by remember { mutableStateOf(0) }
    
    val context = LocalContext.current
    
    // Load SignML content
    LaunchedEffect(signName) {
        loadSignMLFile(context, signName) { content, error ->
            sigmlContent = content
            errorMessage = error
            isLoading = false
            if (content != null && autoPlay) {
                isPlaying = true
            }
        }
    }
    
    // Animation logic
    LaunchedEffect(isPlaying, sigmlContent) {
        if (isPlaying && sigmlContent != null) {
            val totalFrames = calculateTotalFrames(sigmlContent!!)
            for (frame in 0 until totalFrames) {
                currentFrame = frame
                delay(100) // 10 FPS animation
            }
            isPlaying = false
            onPlayComplete()
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
            // Animation Display Area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = Color(0xFFE8F5E8),
                border = BorderStroke(2.dp, Color(0xFF4CAF50))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator(color = Color(0xFF4CAF50))
                        }
                        errorMessage != null -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color(0xFFE57373)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Unable to load sign",
                                    color = Color(0xFFE57373),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = signName,
                                    color = Color(0xFF666666),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        sigmlContent != null -> {
                            SignAnimationCanvas(
                                sigmlContent = sigmlContent!!,
                                currentFrame = currentFrame,
                                isPlaying = isPlaying,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sign Information
            Text(
                text = signName.uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            if (isPlaying) {
                Text(
                    text = "Playing animation...",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Control Buttons
            if (showControls) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (sigmlContent != null) {
                                isPlaying = !isPlaying
                                if (isPlaying) {
                                    currentFrame = 0
                                }
                            }
                        },
                        enabled = sigmlContent != null && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isPlaying) "Pause" else "Play")
                    }
                    
                    OutlinedButton(
                        onClick = {
                            if (sigmlContent != null) {
                                currentFrame = 0
                                isPlaying = true
                            }
                        },
                        enabled = sigmlContent != null && !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Replay",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Replay")
                    }
                }
            }
        }
    }
}

@Composable
fun SignAnimationCanvas(
    sigmlContent: String,
    currentFrame: Int,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Try to load the same human avatar used in the translator
    val humanAvatarResId = remember {
        context.resources.getIdentifier("human_avatar", "drawable", context.packageName)
    }
    
    // Subtle breathing animation for consistency with translator avatar
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
    
    // Emoji fallback sequence if no drawable exists (consistent with translator)
    val animatedEmojis = remember { listOf("🤟", "👋", "🙏", "👍", "✋", "👌", "🤲", "👐", "🖐️", "✌️") }
    var currentEmojiIndex by remember { mutableStateOf(0) }
    
    LaunchedEffect(isPlaying, sigmlContent) {
        if (humanAvatarResId == 0 && isPlaying) {
            // Emoji fallback animation
            while (isPlaying) {
                delay(500)
                currentEmojiIndex = (currentEmojiIndex + 1) % animatedEmojis.size
            }
        }
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
        border = BorderStroke(2.dp, Color(0xFF4CAF50))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (humanAvatarResId != 0) {
                // Use the same human avatar as translator with breathing animation
                Image(
                    painter = painterResource(id = humanAvatarResId),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                // Fallback: use emoji animation (same as translator)
                Text(
                    text = if (isPlaying) animatedEmojis[currentEmojiIndex] else "🤟",
                    fontSize = 48.sp
                )
            }
            
            if (isPlaying) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Signing...",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

fun DrawScope.drawAnimatedFigure(
    centerX: Float,
    centerY: Float,
    frame: Int,
    rotation: Float,
    sigmlContent: String
) {
    val primaryColor = Color(0xFF4CAF50)
    val secondaryColor = Color(0xFF2196F3)
    
    // Parse basic movements from SignML
    val hasHandMovement = sigmlContent.contains("hamflathand") || sigmlContent.contains("hamfist")
    val hasArmMovement = sigmlContent.contains("hammove")
    
    // Draw basic human figure
    translate(centerX, centerY) {
        // Head
        drawCircle(
            color = primaryColor,
            radius = 15f,
            center = Offset(0f, -80f)
        )
        
        // Body
        drawLine(
            color = primaryColor,
            start = Offset(0f, -65f),
            end = Offset(0f, 20f),
            strokeWidth = 8f
        )
        
        // Arms with animation
        val armOffset = if (hasArmMovement) sin((frame * PI / 10).toFloat()) * 10f else 0f
        val leftArmEnd = Offset(-30f + armOffset, -20f)
        val rightArmEnd = Offset(30f - armOffset, -20f)
        
        // Left arm
        drawLine(
            color = secondaryColor,
            start = Offset(0f, -40f),
            end = leftArmEnd,
            strokeWidth = 6f
        )
        
        // Right arm
        drawLine(
            color = secondaryColor,
            start = Offset(0f, -40f),
            end = rightArmEnd,
            strokeWidth = 6f
        )
        
        // Hands with animation
        if (hasHandMovement) {
            val handSize = 10f + sin((frame * PI / 5).toFloat()) * 4f
            // Left hand (orange) and right hand (teal) for easy distinction
            drawCircle(
                color = Color(0xFFFF9800),
                radius = handSize,
                center = leftArmEnd
            )
            drawCircle(
                color = Color(0xFF26C6DA),
                radius = handSize,
                center = rightArmEnd
            )
        } else {
            drawCircle(
                color = Color(0xFFFF9800),
                radius = 8f,
                center = leftArmEnd
            )
            drawCircle(
                color = Color(0xFF26C6DA),
                radius = 8f,
                center = rightArmEnd
            )
        }
        
        // Legs
        drawLine(
            color = primaryColor,
            start = Offset(0f, 20f),
            end = Offset(-20f, 60f),
            strokeWidth = 6f
        )
        drawLine(
            color = primaryColor,
            start = Offset(0f, 20f),
            end = Offset(20f, 60f),
            strokeWidth = 6f
        )
        
        // Movement indicators
        if (hasArmMovement) {
            drawCircle(
                color = Color(0xFF4CAF50).copy(alpha = 0.3f),
                radius = 50f + sin((frame * PI / 8).toFloat()) * 10f,
                center = Offset(0f, -40f)
            )
        }
    }
}

private fun loadSignMLFile(
    context: Context,
    signName: String,
    callback: (String?, String?) -> Unit
) {
    try {
        // Try to load the .sigml file from assets
        val fileName = when {
            signName.matches(Regex("\\d+")) -> "$signName.sigml" // Numbers
            signName.length == 1 && signName.matches(Regex("[a-zA-Z]")) -> {
                // Single letters - map to numbers (A=65, B=66, etc.)
                "${signName.uppercase().first().code - 'A'.code + 65}.sigml"
            }
            else -> "${signName.lowercase()}.sigml"
        }
        
        val inputStream = context.assets.open("signs/$fileName")
        val content = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        
        callback(content, null)
    } catch (e: IOException) {
        callback(null, "Sign file not found: $signName")
    }
}

private fun calculateTotalFrames(sigmlContent: String): Int {
    // Simple frame calculation based on SignML content
    val baseFrames = 30 // 3 seconds at 10 FPS
    val complexity = sigmlContent.length / 100
    return baseFrames + complexity.coerceAtMost(20)
}

/**
 * Simple Sign Display component for showing static sign information
 */
@Composable
fun SimpleSignCard(
    signName: String,
    description: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sign representation
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.1f),
                        RoundedCornerShape(40.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = signName.uppercase(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = signName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}