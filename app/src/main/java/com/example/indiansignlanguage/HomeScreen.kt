package com.example.indiansignlanguage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.indiansignlanguage.data.UserProfile
import com.example.indiansignlanguage.utils.FirebaseUtils
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Dummy data classes for the UI
data class LearningItem(val name: String, val imageUrl: String)
data class UserProgress(
    val name: String,
    val avatarUrl: String,
    val progress: Float,
    val modules: Int,
    val alphabetsLearned: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Load user profile when screen opens
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            FirebaseUtils.getUserProfile().fold(
                onSuccess = { profile ->
                    userProfile = profile
                    isLoading = false
                },
                onFailure = {
                    // Create default profile if none exists
                    userProfile = UserProfile(
                        fullName = "User",
                        email = "",
                        totalLessonsCompleted = 0,
                        currentLevel = 1,
                        totalScore = 0
                    )
                    isLoading = false
                }
            )
        }
    }

    val learningHubItems = listOf(
        LearningItem("Thank you", "https://placehold.co/200x160/eef5ff/333?text=Sign"),
        LearningItem("Yes", "https://placehold.co/200x160/eef5ff/333?text=Sign"),
        LearningItem("Please", "https://placehold.co/200x160/eef5ff/333?text=Sign"),
        LearningItem("Help", "https://placehold.co/200x160/eef5ff/333?text=Sign"),
        LearningItem("Friend", "https://placehold.co/200x160/eef5ff/333?text=Sign")
    )

    // Using a separate variable to avoid potential recomposition issues with the nullable userProfile
    val currentProfile = userProfile
    val userProgress = UserProgress(
        name = currentProfile?.fullName ?: "Loading...",
        avatarUrl = "https://placehold.co/80x80/007AFF/FFFFFF?text=${currentProfile?.fullName?.take(2)?.uppercase() ?: "U"}",
        progress = if (currentProfile != null && currentProfile.totalLessonsCompleted > 0)
            (currentProfile.totalLessonsCompleted.toFloat() / 100f).coerceAtMost(1f)
        else 0f,
        modules = currentProfile?.completedModules?.size ?: 0,
        alphabetsLearned = currentProfile?.totalLessonsCompleted ?: 0
    )

    Scaffold(
        topBar = { TopBar(navController) },
        containerColor = Color(0xFFF2F2F7)
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                SearchBar(navController = navController)
                Spacer(modifier = Modifier.height(20.dp))
                QuickAccessSection(navController)
                Spacer(modifier = Modifier.height(25.dp))
                ProgressSection(progress = userProgress, navController = navController)
                Spacer(modifier = Modifier.height(25.dp))
                LearningHubSection(items = learningHubItems, navController = navController)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Indian Sign Language", fontWeight = FontWeight.SemiBold) },
        actions = {
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Person, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun SearchBar(navController: NavController) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE5E5EA), shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color(0xFF8E8E93)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text("Search for signs, words, or phrases...", color = Color(0xFF8E8E93))
                }
                innerTextField()
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchText.isNotBlank()) {
                        keyboardController?.hide()
                        val encodedText = URLEncoder.encode(searchText, StandardCharsets.UTF_8.toString())
                        navController.navigate("translator/$encodedText")
                    }
                }
            )
        )
    }
}

@Composable
fun QuickAccessSection(navController: NavController) {
    Column {
        Text("Quick Access", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickAccessItem(
                icon = Icons.Default.Person,
                text = "Greetings"
            ) {
                navController.navigate("greetings")
            }
            QuickAccessItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                text = "Learn on Words"
            ) {
                navController.navigate("commonwords")
            }
            QuickAccessItem(
                icon = Icons.Default.DateRange,
                text = "Daily Practice"
            ) {
                navController.navigate("modules")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAccessItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .size(width = 110.dp, height = 100.dp)
            .border(1.dp, Color(0xFFE5E5EA), shape = RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = Color(0xFF007AFF), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3C3C43))
        }
    }
}

@Composable
fun ProgressSection(progress: UserProgress, navController: NavController) {
    Column {
        Text("Your Progress", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE5E5EA), shape = RoundedCornerShape(15.dp))
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Image(
                        painter = rememberAsyncImagePainter(progress.avatarUrl),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(progress.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Signs Learning: ${progress.modules} Modules", fontSize = 12.sp, color = Color(0xFF8E8E93))
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate("modules") },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                        ) {
                            Text("Get Started", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 10.dp)) {
                    CircularProgressIndicator(
                        progress = { progress.progress }, // Corrected: Pass the float directly
                        modifier = Modifier.size(60.dp),
                        strokeWidth = 6.dp,
                        color = Color(0xFF007AFF),
                        trackColor = Color(0xFFE5E5EA)
                    )
                    Text("${(progress.progress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFF007AFF))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(progress.alphabetsLearned.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Alphabets", fontSize = 12.sp, color = Color(0xFF8E8E93))
                }
            }
        }
    }
}

@Composable
fun LearningHubSection(items: List<LearningItem>, navController: NavController) {
    Column {
        Text("Learning Hub", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(items) { item ->
                LearningHubItem(item = item, onClick = { navController.navigate("commonwords") })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningHubItem(item: LearningItem, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF3C3C43))
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun HomeScreenPreview() { // Renamed for clarity, following convention
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}
