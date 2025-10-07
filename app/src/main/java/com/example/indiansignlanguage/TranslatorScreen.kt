package com.example.indiansignlanguage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import com.example.indiansignlanguage.utils.SiGMLPlayerManager
import com.example.indiansignlanguage.components.OfflineAvatarPlayer
import com.example.indiansignlanguage.components.AssetVideoPlayer
import com.example.indiansignlanguage.components.assetVideoUriOrNull
import com.example.indiansignlanguage.components.toVideoFileName
import com.example.indiansignlanguage.data.SignDataManager
import android.util.Log

// Define colors for the translator screen
val translatorPrimaryBlue = Color(0xFF0D62FF)
val translatorLightBlue = Color(0xFFE0EAFC)
val translatorDarkText = Color(0xFF1D2B4F)
val translatorLightText = Color(0xFF75819D)
val translatorBackgroundColor = Color(0xFFF7F9FC)
val translatorAccentColor = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(navController: NavController) {
    // Log when TranslatorScreen loads
    LaunchedEffect(Unit) {
        Log.d("Navigation", "TranslatorScreen composable loaded and displayed")
    }

    var inputText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }
    var translatedText by remember { mutableStateOf("") }
    var showAvatar by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sigmlPlayerManager = remember { SiGMLPlayerManager(context) }
    val signDataManager = remember { SignDataManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ISL Translator",
                        fontWeight = FontWeight.SemiBold,
                        color = translatorDarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = translatorDarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = translatorBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header Section
            Text(
                text = "Text to Sign Language",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = translatorDarkText,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Type your message to see it in Indian Sign Language",
                fontSize = 16.sp,
                color = translatorLightText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Input Section
            InputSection(
                inputText = inputText,
                onInputChange = { inputText = it },
                onTranslate = {
                    if (inputText.isNotBlank()) {
                        isTranslating = true
                        translatedText = inputText
                        showAvatar = true

                        coroutineScope.launch {
                            // Analyze input text for available signs
                            val availableSigns = signDataManager.findAvailableSignsInText(inputText)
                            if (availableSigns.isEmpty()) {
                                // Show message about no available signs, but still attempt translation
                                Toast.makeText(context, "No offline signs were found, but we'll try our best!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Found ${availableSigns.size} signs. Starting translation...", Toast.LENGTH_SHORT).show()
                            }
                            delay(500) // Allow time for user to see the feedback
                            isTranslating = false
                        }
                    } else {
                        Toast.makeText(context, "Please enter some text to translate", Toast.LENGTH_SHORT).show()
                    }
                },
                isTranslating = isTranslating
            )

            Spacer(modifier = Modifier.height(24.dp))

// Avatar Display Section
            if (showAvatar && translatedText.isNotBlank()) {
                // Determine availability of offline signs using SignDataManager
                var hasOfflineSigns by remember { mutableStateOf(false) }
                var availableSignsCount by remember { mutableStateOf(0) }
                LaunchedEffect(translatedText) {
                    val availableSigns = signDataManager.findAvailableSignsInText(translatedText)
                    hasOfflineSigns = availableSigns.isNotEmpty()
                    availableSignsCount = availableSigns.size
                }

                // Try pre-rendered video first (phrase), then first word
                val phraseVideoUri = remember(translatedText) {
                    assetVideoUriOrNull(context, toVideoFileName(translatedText))
                }
                val firstWordVideoUri = remember(translatedText) {
                    translatedText
                        .split(" ", ",", ".", "!", "?", ";")
                        .map { it.trim() }
                        .firstOrNull { it.isNotEmpty() }
                        ?.let { assetVideoUriOrNull(context, toVideoFileName(it)) }
                }
                val videoUri = phraseVideoUri ?: firstWordVideoUri

Column {
                    when {
                        videoUri != null -> {
                            AssetVideoPlayer(
                                assetUri = videoUri,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 300.dp, max = 500.dp)
                            )
                            isTranslating = false
                        }
                        hasOfflineSigns -> {
                            OfflineAvatarPlayer(
                                text = translatedText,
                                sigmlPlayerManager = sigmlPlayerManager,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 300.dp, max = 500.dp),
                                onSignComplete = { isTranslating = false }
                            )
                        }
                        else -> {
                            // No offline resources found - show suggestions
                            DefaultAvatarPlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                isTranslating = false
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "No offline signs available for \"$translatedText\"",
                                    color = translatorLightText,
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Suggest similar available signs
                                val suggestions = signDataManager.searchSigns(translatedText).take(3)
                                if (suggestions.isNotEmpty()) {
                                    Text(
                                        text = "Try these available signs:",
                                        color = translatorDarkText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        suggestions.forEach { suggestion ->
                                            FilterChip(
                                                onClick = {
                                                    inputText = suggestion.word
                                                    isTranslating = true
                                                    translatedText = suggestion.word
                                                    showAvatar = true
                                                    coroutineScope.launch { delay(300) }
                                                },
                                                label = {
                                                    Text(
                                                        suggestion.word,
                                                        fontSize = 10.sp
                                                    )
                                                },
                                                selected = false,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Default avatar placeholder
                DefaultAvatarPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    isTranslating = isTranslating
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Phrases Section
QuickPhrasesSection(
                onPhraseSelected = { phrase ->
                    inputText = phrase
                    isTranslating = true
                    translatedText = phrase
                    showAvatar = true
                    coroutineScope.launch { delay(300) }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InputSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onTranslate: () -> Unit,
    isTranslating: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Enter Text",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = translatorDarkText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                placeholder = { Text("Type your message here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = translatorPrimaryBlue,
                    unfocusedBorderColor = Color(0xFFE5E5EA)
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onTranslate,
                enabled = !isTranslating && inputText.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = translatorPrimaryBlue,
                    disabledContainerColor = translatorLightBlue
                )
            ) {
                if (isTranslating) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Translating...")
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Translate",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Translate to Sign Language", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun DefaultAvatarPlaceholder(
    modifier: Modifier = Modifier,
    isTranslating: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    color = if (isTranslating) translatorAccentColor.copy(alpha = 0.2f) else translatorLightBlue
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (isTranslating) {
                            CircularProgressIndicator(
                                color = translatorAccentColor,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                tint = translatorPrimaryBlue,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (isTranslating) "Preparing Avatar..." else "Avatar will appear here",
                    color = translatorLightText,
                    fontSize = 16.sp
                )
                Text(
                    text = if (isTranslating) "Loading sign language display" else "Enter text and tap translate to start",
                    color = translatorLightText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuickPhrasesSection(onPhraseSelected: (String) -> Unit) {
    // Expanded phrases organized by category with available SignML files
    val quickPhrasesCategories = mapOf(
        "Greetings" to listOf(
            "Hello", "Thank you", "Please", "Sorry", "Welcome",
            "Good morning", "Good evening", "Good night", "See you later"
        ),
        "Daily Needs" to listOf(
            "Help", "Water", "Food", "Eat", "Drink", "Money",
            "Work", "Sleep", "Home", "School", "Hospital"
        ),
        "Responses" to listOf(
            "Yes", "No", "Good", "Bad", "Right", "Wrong", "Like", "Love"
        ),
        "Family" to listOf(
            "Mother", "Father", "Brother", "Sister", "Child", "Baby", "Friend"
        ),
        "Time" to listOf(
            "Today", "Tomorrow", "Yesterday", "Morning", "Afternoon", "Evening", "Night"
        ),
        "Actions" to listOf(
            "Go", "Come", "Stop", "Wait", "Look", "Sit", "Stand", "Give", "Take"
        ),
        "Emotions" to listOf(
            "Happy", "Sad", "Angry", "Fear", "Pain"
        ),
        "Objects" to listOf(
            "Book", "Phone", "Car", "Door", "Window", "Chair", "Table", "Bed"
        )
    )

    // Flatten all phrases for display
    val allQuickPhrases = quickPhrasesCategories.values.flatten()

    Column {
        Text(
            text = "Quick Phrases",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = translatorDarkText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Tap any phrase to instantly translate to sign language",
            fontSize = 14.sp,
            color = translatorLightText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display phrases by category
        quickPhrasesCategories.forEach { (category, phrases) ->
            Text(
                text = category,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = translatorDarkText,
                modifier = Modifier.padding(bottom = 8.dp, top = 12.dp)
            )

            // Create wrapped layout for phrases in this category
            val chunkedPhrases = phrases.chunked(3)
            chunkedPhrases.forEach { rowPhrases ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowPhrases.forEach { phrase ->
                        FilterChip(
                            onClick = { onPhraseSelected(phrase) },
                            label = {
                                Text(
                                    phrase,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                            },
                            selected = false,
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                labelColor = translatorDarkText,
                                selectedContainerColor = translatorLightBlue,
                                selectedLabelColor = translatorPrimaryBlue
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = translatorLightBlue,
                                selectedBorderColor = translatorPrimaryBlue,
                                enabled = true,
                                selected = false
                            )
                        )
                    }

                    // Fill remaining space if needed
                    repeat(3 - rowPhrases.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TranslatorScreenPreview() {
    TranslatorScreen(navController = rememberNavController())
}