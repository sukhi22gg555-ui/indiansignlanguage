package com.example.indiansignlanguage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.indiansignlanguage.components.SignMLPlayer
import com.example.indiansignlanguage.components.SimpleSignCard

data class CommonWordItem(
    val word: String,
    val category: String,
    val description: String,
    val signId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonWordsScreen(navController: NavController) {
    var selectedWord by remember { mutableStateOf<String?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }
    
    // Comprehensive common words organized by category (using available SignML files)
    val commonWords = remember {
        listOf(
            // Essential Greetings
            CommonWordItem("Hello", "Greetings", "Basic greeting sign", "hello"),
            CommonWordItem("Thank You", "Greetings", "Expression of gratitude", "thankyou"),
            CommonWordItem("Please", "Greetings", "Polite request sign", "please"),
            CommonWordItem("Sorry", "Greetings", "Apology sign", "sorry"),
            CommonWordItem("Welcome", "Greetings", "Welcome greeting", "welcome"),
            
            // Basic Responses
            CommonWordItem("Yes", "Responses", "Affirmative response", "yes"),
            CommonWordItem("No", "Responses", "Negative response", "no"),
            CommonWordItem("Good", "Responses", "Positive response", "good"),
            CommonWordItem("Bad", "Responses", "Negative response", "bad"),
            CommonWordItem("Right", "Responses", "Correct or direction", "right"),
            CommonWordItem("Wrong", "Responses", "Incorrect response", "wrong"),
            
            // Family & People
            CommonWordItem("Mother", "Family", "Sign for mother", "mother"),
            CommonWordItem("Father", "Family", "Sign for father", "father"),
            CommonWordItem("Brother", "Family", "Sign for brother", "brother"),
            CommonWordItem("Sister", "Family", "Sign for sister", "sister"),
            CommonWordItem("Child", "Family", "Sign for child", "child"),
            CommonWordItem("Baby", "Family", "Sign for baby", "baby"),
            CommonWordItem("Friend", "Family", "Sign for friend", "friend"),
            CommonWordItem("Boy", "Family", "Sign for boy", "boy"),
            CommonWordItem("Girl", "Family", "Sign for girl", "girl"),
            CommonWordItem("Man", "Family", "Sign for man", "man"),
            CommonWordItem("Woman", "Family", "Sign for woman", "woman"),
            
            // Daily Needs
            CommonWordItem("Water", "Needs", "Sign for water", "water"),
            CommonWordItem("Food", "Needs", "Sign for food", "food"),
            CommonWordItem("Eat", "Needs", "Sign for eating", "eat"),
            CommonWordItem("Drink", "Needs", "Sign for drinking", "drink"),
            CommonWordItem("Help", "Needs", "Sign for help", "help"),
            CommonWordItem("Money", "Needs", "Sign for money", "money"),
            CommonWordItem("Work", "Needs", "Sign for work", "work"),
            CommonWordItem("Sleep", "Needs", "Sign for sleep", "sleep"),
            
            // Places & Locations
            CommonWordItem("Home", "Places", "Sign for home", "home"),
            CommonWordItem("School", "Places", "Sign for school", "school"),
            CommonWordItem("Hospital", "Places", "Sign for hospital", "hospital"),
            CommonWordItem("Shop", "Places", "Sign for shop", "shop"),
            CommonWordItem("Office", "Places", "Sign for office", "office"),
            CommonWordItem("Hotel", "Places", "Sign for hotel", "hotel"),
            CommonWordItem("Bank", "Places", "Sign for bank", "bank"),
            CommonWordItem("City", "Places", "Sign for city", "city"),
            
            // Time & Schedule
            CommonWordItem("Today", "Time", "Sign for today", "today"),
            CommonWordItem("Tomorrow", "Time", "Sign for tomorrow", "tomorrow"),
            CommonWordItem("Yesterday", "Time", "Sign for yesterday", "yesterday"),
            CommonWordItem("Time", "Time", "Sign for time", "time"),
            CommonWordItem("Morning", "Time", "Sign for morning", "morning"),
            CommonWordItem("Afternoon", "Time", "Sign for afternoon", "afternoon"),
            CommonWordItem("Evening", "Time", "Sign for evening", "evening"),
            CommonWordItem("Night", "Time", "Sign for night", "night"),
            CommonWordItem("Week", "Time", "Sign for week", "week"),
            CommonWordItem("Month", "Time", "Sign for month", "month"),
            CommonWordItem("Year", "Time", "Sign for year", "year"),
            
            // Colors
            CommonWordItem("Red", "Colors", "Sign for red color", "red"),
            CommonWordItem("Blue", "Colors", "Sign for blue color", "blue"),
            CommonWordItem("Green", "Colors", "Sign for green color", "green"),
            CommonWordItem("Yellow", "Colors", "Sign for yellow color", "yellow"),
            CommonWordItem("Black", "Colors", "Sign for black color", "black"),
            CommonWordItem("White", "Colors", "Sign for white color", "white"),
            CommonWordItem("Brown", "Colors", "Sign for brown color", "brown"),
            
            // Common Actions
            CommonWordItem("Go", "Actions", "Sign for go", "go"),
            CommonWordItem("Come", "Actions", "Sign for come", "come"),
            CommonWordItem("Stop", "Actions", "Sign for stop", "stop"),
            CommonWordItem("Wait", "Actions", "Sign for wait", "wait"),
            CommonWordItem("Look", "Actions", "Sign for look", "look"),
            CommonWordItem("Sit", "Actions", "Sign for sit", "sit"),
            CommonWordItem("Stand", "Actions", "Sign for stand", "stand"),
            CommonWordItem("Walk", "Actions", "Sign for walk", "walk"),
            CommonWordItem("Run", "Actions", "Sign for run", "run"),
            CommonWordItem("Give", "Actions", "Sign for give", "give"),
            CommonWordItem("Take", "Actions", "Sign for take", "take"),
            CommonWordItem("Open", "Actions", "Sign for open", "open"),
            CommonWordItem("Close", "Actions", "Sign for close", "close"),
            
            // Emotions & Feelings
            CommonWordItem("Happy", "Emotions", "Sign for happy", "happy"),
            CommonWordItem("Sad", "Emotions", "Sign for sad", "sad"),
            CommonWordItem("Angry", "Emotions", "Sign for angry", "angry"),
            CommonWordItem("Love", "Emotions", "Sign for love", "love"),
            CommonWordItem("Like", "Emotions", "Sign for like", "like"),
            CommonWordItem("Hate", "Emotions", "Sign for dislike", "hate"),
            CommonWordItem("Fear", "Emotions", "Sign for fear", "fear"),
            
            // Essential Objects
            CommonWordItem("Book", "Objects", "Sign for book", "book"),
            CommonWordItem("Phone", "Objects", "Sign for phone", "phone"),
            CommonWordItem("Car", "Objects", "Sign for car", "car"),
            CommonWordItem("House", "Objects", "Sign for house", "house"),
            CommonWordItem("Door", "Objects", "Sign for door", "door"),
            CommonWordItem("Window", "Objects", "Sign for window", "window"),
            CommonWordItem("Chair", "Objects", "Sign for chair", "chair"),
            CommonWordItem("Table", "Objects", "Sign for table", "table"),
            CommonWordItem("Bed", "Objects", "Sign for bed", "bed"),
            CommonWordItem("Bag", "Objects", "Sign for bag", "bag"),
            CommonWordItem("Key", "Objects", "Sign for key", "key"),
            
            // Weather & Nature
            CommonWordItem("Sun", "Weather", "Sign for sun", "sun"),
            CommonWordItem("Rain", "Weather", "Sign for rain", "rain"),
            CommonWordItem("Hot", "Weather", "Sign for hot weather", "hot"),
            CommonWordItem("Cold", "Weather", "Sign for cold weather", "cold"),
            CommonWordItem("Wind", "Weather", "Sign for wind", "wind"),
            CommonWordItem("Tree", "Nature", "Sign for tree", "tree"),
            CommonWordItem("Flower", "Nature", "Sign for flower", "flower"),
            
            // Transportation
            CommonWordItem("Bus", "Transport", "Sign for bus", "bus"),
            CommonWordItem("Train", "Transport", "Sign for train", "train"),
            CommonWordItem("Plane", "Transport", "Sign for airplane", "airplane"),
            CommonWordItem("Bike", "Transport", "Sign for bicycle", "bike"),
            
            // Health & Body
            CommonWordItem("Doctor", "Health", "Sign for doctor", "doctor"),
            CommonWordItem("Medicine", "Health", "Sign for medicine", "medicine"),
            CommonWordItem("Pain", "Health", "Sign for pain", "pain"),
            CommonWordItem("Healthy", "Health", "Sign for healthy", "healthy"),
            CommonWordItem("Sick", "Health", "Sign for sick", "sick")
        )
    }
    
    val groupedWords = remember { commonWords.groupBy { it.category } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Indian Sign Language - Common Words", 
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
        
        if (selectedWord != null) {
            // Individual word view with sequential navigation
            IndividualWordView(
                commonWords = commonWords,
                currentIndex = currentIndex,
                onNavigateBack = { selectedWord = null },
                onNavigateNext = {
                    if (currentIndex < commonWords.size - 1) {
                        currentIndex++
                        selectedWord = commonWords[currentIndex].word
                    }
                },
                onNavigatePrevious = {
                    if (currentIndex > 0) {
                        currentIndex--
                        selectedWord = commonWords[currentIndex].word
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // Categorized view of all words
            CommonWordsGridView(
                groupedWords = groupedWords,
                commonWords = commonWords,
                onWordClick = { index ->
                    currentIndex = index
                    selectedWord = commonWords[index].word
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun CommonWordsGridView(
    groupedWords: Map<String, List<CommonWordItem>>,
    commonWords: List<CommonWordItem>,
    onWordClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Learn Common Indian Sign Language Words",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Essential words organized by category. Tap on any word to learn its sign.",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Display words by category
        groupedWords.forEach { (category, words) ->
            item {
                CategorySection(
                    category = category,
                    words = words,
                    commonWords = commonWords,
                    onWordClick = onWordClick
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CategorySection(
    category: String,
    words: List<CommonWordItem>,
    commonWords: List<CommonWordItem>,
    onWordClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                Text(
                    text = "${words.size} words",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Create grid of words
            words.chunked(2).forEach { rowWords ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowWords.forEach { word ->
                        val actualIndex = commonWords.indexOf(word)
                        SimpleSignCard(
                            signName = word.word,
                            description = word.description,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.2f),
                            onClick = { onWordClick(actualIndex) }
                        )
                    }
                    // Fill empty space if odd number of words
                    if (rowWords.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                
                if (rowWords != words.chunked(2).last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun IndividualWordView(
    commonWords: List<CommonWordItem>,
    currentIndex: Int,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit,
    onNavigatePrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
val currentItem = commonWords[currentIndex]
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
                progress = { (currentIndex + 1).toFloat() / commonWords.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE0E0E0)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Word ${currentIndex + 1} of ${commonWords.size}",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            SignMLPlayer(
                signName = currentItem.signId,
                modifier = Modifier.fillMaxWidth(),
                autoPlay = true,
                showControls = true
            )
        }
        
        item {
            // Word information
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
                    // Category badge
                    Surface(
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = currentItem.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    
                    Text(
                        text = currentItem.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
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
                
                // Back to categories button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("All Words")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Next button
                Button(
                    onClick = onNavigateNext,
                    enabled = currentIndex < commonWords.size - 1,
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
            // Completion message for last word
            if (currentIndex == commonWords.size - 1) {
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
                            text = "🎉 Excellent Work!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "You've completed the Common Words module! You now know essential signs for daily communication.",
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
