package com.example.indiansignlanguage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness3
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indiansignlanguage.components.SignMLPlayer

// --- Data class to hold the greeting text, icon, and SignML identifier ---
data class GreetingItem(
    val text: String, 
    val icon: ImageVector, 
    val signId: String,
    val description: String
)

// --- Main Composable for the Greetings Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingsScreen(navController: androidx.navigation.NavController) {
    var selectedGreeting by remember { mutableStateOf<GreetingItem?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }

    // Real greetings with corresponding SignML identifiers
    val greetingItems = remember {
        listOf(
            GreetingItem("Hello", Icons.Default.WavingHand, "hello", "Basic greeting sign"),
            GreetingItem("Good Morning", Icons.Default.LightMode, "good_morning", "Morning greeting"),
            GreetingItem("Good Evening", Icons.Default.Brightness3, "good_evening", "Evening greeting"),
            GreetingItem("Good Night", Icons.Default.DarkMode, "good_night", "Night greeting"),
            GreetingItem("Thank You", Icons.Default.Favorite, "thankyou", "Expression of gratitude"),
            GreetingItem("Please", Icons.Default.ThumbUp, "please", "Polite request"),
            GreetingItem("Sorry", Icons.Default.SentimentVeryDissatisfied, "sorry", "Apology sign"),
            GreetingItem("Welcome", Icons.Default.Person, "welcome", "Welcome greeting"),
            GreetingItem("How are you?", Icons.Default.QuestionAnswer, "how_are_you", "Asking about wellbeing"),
            GreetingItem("Nice to meet you", Icons.Default.Group, "nice_to_meet_you", "First meeting greeting"),
            GreetingItem("See you later", Icons.Default.WavingHand, "see_you_later", "Farewell greeting"),
            GreetingItem("Take care", Icons.Default.FavoriteBorder, "take_care", "Caring farewell")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Indian Sign Language - Greetings",
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

        if (selectedGreeting != null) {
            // Individual greeting view with sequential navigation
            IndividualGreetingView(
                greetingItems = greetingItems,
                currentIndex = currentIndex,
                onNavigateBack = { selectedGreeting = null },
                onNavigateNext = {
                    if (currentIndex < greetingItems.size - 1) {
                        currentIndex++
                        selectedGreeting = greetingItems[currentIndex]
                    }
                },
                onNavigatePrevious = {
                    if (currentIndex > 0) {
                        currentIndex--
                        selectedGreeting = greetingItems[currentIndex]
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // Grid view of all greetings
            GreetingsGridView(
                greetingItems = greetingItems,
                onGreetingClick = { index ->
                    currentIndex = index
                    selectedGreeting = greetingItems[index]
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun GreetingsGridView(
    greetingItems: List<GreetingItem>,
    onGreetingClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Learn Indian Sign Language Greetings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Essential greetings for daily communication. Tap on any greeting to learn its sign.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(greetingItems.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    val actualIndex = greetingItems.indexOf(item)
                    GreetingCard(
                        item = item,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.2f),
                        onClick = { onGreetingClick(actualIndex) }
                    )
                }
                // Fill empty space if odd number of items
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- Composable for a single greeting card in the grid ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingCard(
    item: GreetingItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(BorderStroke(2.dp, Color(0xFF4CAF50)), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun IndividualGreetingView(
    greetingItems: List<GreetingItem>,
    currentIndex: Int,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentItem = greetingItems[currentIndex]

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
                progress = { (currentIndex + 1).toFloat() / greetingItems.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Greeting ${currentIndex + 1} of ${greetingItems.size}",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // Sign player using the same avatar as translator
            SignMLPlayer(
                signName = currentItem.signId,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            // Greeting information
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
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .border(BorderStroke(2.dp, Color(0xFF4CAF50)), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = currentItem.icon,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = currentItem.text,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center
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
                    Text("All Greetings")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Next button
                Button(
                    onClick = onNavigateNext,
                    enabled = currentIndex < greetingItems.size - 1,
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
            // Completion message for last greeting
            if (currentIndex == greetingItems.size - 1) {
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
                            text = "🎉 Wonderful!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "You've completed the Greetings module! You can now greet people and express gratitude in sign language.",
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


// --- Preview for Android Studio ---
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun GreetingsScreenPreview() {
    MaterialTheme {
        GreetingsScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}
