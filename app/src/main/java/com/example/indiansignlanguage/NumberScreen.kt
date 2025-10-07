package com.example.indiansignlanguage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

data class NumberItem(
    val number: Int,
    val description: String,
    val signId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumbersScreen(navController: NavController) {
    var selectedNumber by remember { mutableStateOf<String?>(null) }
    var currentIndex by remember { mutableStateOf(0) }
    
    // Numbers 0-100 with their descriptions
    val numberItems = remember {
        (0..100).map { number ->
            NumberItem(
                number = number,
                description = "Sign for number $number",
                signId = number.toString()
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Indian Sign Language - Numbers", 
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
        
        if (selectedNumber != null) {
            // Individual number view with sequential navigation
            IndividualNumberView(
                numberItems = numberItems,
                currentIndex = currentIndex,
                onNavigateBack = { selectedNumber = null },
                onNavigateNext = {
                    if (currentIndex < numberItems.size - 1) {
                        currentIndex++
                        selectedNumber = numberItems[currentIndex].number.toString()
                    }
                },
                onNavigatePrevious = {
                    if (currentIndex > 0) {
                        currentIndex--
                        selectedNumber = numberItems[currentIndex].number.toString()
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // Grid view of all numbers
            NumbersGridView(
                numberItems = numberItems,
                onNumberClick = { index ->
                    currentIndex = index
                    selectedNumber = numberItems[index].number.toString()
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun NumbersGridView(
    numberItems: List<NumberItem>,
    onNumberClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Learn Indian Sign Language Numbers",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Tap on any number to learn its sign. Navigate through them sequentially.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Group numbers by tens for better organization
        items(numberItems.chunked(10)) { chunk ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val rangeStart = chunk.first().number
                    val rangeEnd = chunk.last().number
                    Text(
                        text = "Numbers $rangeStart - $rangeEnd",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Create rows of numbers (5 per row)
                    chunk.chunked(5).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { item ->
                                val actualIndex = numberItems.indexOf(item)
                                SimpleSignCard(
                                    signName = item.number.toString(),
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    onClick = { onNumberClick(actualIndex) }
                                )
                            }
                            // Fill empty spaces if needed
                            repeat(5 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        
                        if (rowItems != chunk.chunked(5).last()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun IndividualNumberView(
    numberItems: List<NumberItem>,
    currentIndex: Int,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
val currentItem = numberItems[currentIndex]
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
                progress = { (currentIndex + 1).toFloat() / numberItems.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Number ${currentIndex + 1} of ${numberItems.size}",
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
            // Number information
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
                        text = "Number: ${currentItem.number}",
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
                    Text("All Numbers")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Next button
                Button(
                    onClick = onNavigateNext,
                    enabled = currentIndex < numberItems.size - 1,
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
            // Completion message for last number
            if (currentIndex == numberItems.size - 1) {
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
                            text = "You've completed the Numbers module! You can now count and express numerical values in sign language.",
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

