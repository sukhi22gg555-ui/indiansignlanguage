package com.example.indiansignlanguage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.indiansignlanguage.components.SignMLPlayer
import com.example.indiansignlanguage.components.SimpleSignCard

data class AlphabetItem(
    val letter: String,
    val description: String,
    val signId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphabetsScreen(navController: NavController) {
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var currentIndex by remember { mutableStateOf(0) }
    
    // All alphabet letters with their descriptions
    val alphabetItems = remember {
        ('A'..'Z').mapIndexed { index, letter ->
            AlphabetItem(
                letter = letter.toString(),
                description = "Sign for letter $letter",
                signId = (65 + index).toString() // Map A=65, B=66, etc.
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Indian Sign Language - Alphabets", 
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
        
        if (selectedLetter != null) {
            // Individual letter view with sequential navigation
            IndividualLetterView(
                alphabetItems = alphabetItems,
                currentIndex = currentIndex,
                onNavigateBack = { selectedLetter = null },
                onNavigateNext = {
                    if (currentIndex < alphabetItems.size - 1) {
                        currentIndex++
                        selectedLetter = alphabetItems[currentIndex].letter
                    }
                },
                onNavigatePrevious = {
                    if (currentIndex > 0) {
                        currentIndex--
                        selectedLetter = alphabetItems[currentIndex].letter
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // Grid view of all letters
            AlphabetsGridView(
                alphabetItems = alphabetItems,
                onLetterClick = { index ->
                    currentIndex = index
                    selectedLetter = alphabetItems[index].letter
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun AlphabetsGridView(
    alphabetItems: List<AlphabetItem>,
    onLetterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Learn Indian Sign Language Alphabets",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Tap on any letter to learn its sign. Navigate through them sequentially.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        items(alphabetItems.chunked(3)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEachIndexed { index, item ->
                    val actualIndex = alphabetItems.indexOf(item)
                    SimpleSignCard(
                        signName = item.letter,
                        description = item.description,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onLetterClick(actualIndex) }
                    )
                }
                // Fill empty spaces if needed
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun IndividualLetterView(
    alphabetItems: List<AlphabetItem>,
    currentIndex: Int,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentItem = alphabetItems[currentIndex]
// Online avatar removed; using offline player only
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress indicator
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / alphabetItems.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Letter ${currentIndex + 1} of ${alphabetItems.size}",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
// Sign player (offline only)
            SignMLPlayer(
                signName = currentItem.signId,
                modifier = Modifier.fillMaxWidth(),
                autoPlay = true,
                showControls = true
            )
        }
        
        item {
            // Letter information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Letter: ${currentItem.letter}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = currentItem.description,
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )
                    
                }
            }
        }
        
        item {
            // Navigation controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous button
                OutlinedButton(
                    onClick = onNavigatePrevious,
                    enabled = currentIndex > 0,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Previous")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Back to grid button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("All Letters")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Next button
                Button(
                    onClick = onNavigateNext,
                    enabled = currentIndex < alphabetItems.size - 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next")
                }
            }
        }
        
        item {
            // Completion message for last letter
            if (currentIndex == alphabetItems.size - 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 Congratulations!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "You've completed the Alphabets module! You can now form words using individual letters.",
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}