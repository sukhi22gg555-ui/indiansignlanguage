package com.example.indiansignlanguage

// This line tells our file which other pieces of code it needs to use from the Jetpack Compose library.
// We need these for creating layouts, text, images, icons, etc.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// This is the line that fixes the error. It tells the code where to find your project's resources (like images).

/*
 * --- What is a "Data Class"? ---
 * A data class is a simple box for holding data. It's a clean way to organize information.
 */

// This data class holds the info for a single category button (like "Greetings").
// It just needs a title (the text) and an icon.
data class SignCategory(val title: String, val icon: ImageVector)


/*
 * --- What is a "@Composable" function? ---
 * In Jetpack Compose, a Composable function is a building block for your UI.
 * Think of it like a LEGO brick. You put many of them together to build your screen.
 * Each function describes a piece of the UI.
 */

// This is the main Composable function that builds the entire screen.
@OptIn(ExperimentalMaterial3Api::class) // This is needed for some newer Material 3 components.
@Composable
fun Modules(navController: androidx.navigation.NavController) {

    // --- 1. PREPARE THE DATA ---
    // In a real app, this data would come from the internet or a database.
    // Here, we create some sample data to show in our UI.

    val categories = listOf(
        SignCategory("Greetings", Icons.Default.WavingHand),
        SignCategory("Numbers", Icons.Default.Info),
        SignCategory("Common Words", Icons.Default.Info),
        SignCategory("Alphabets", Icons.Default.Info)
    )

    // --- 2. BUILD THE SCREEN LAYOUT ---
    // `Scaffold` is a special Composable that provides a standard screen layout.
    // It gives us a place for a top bar and the main content area.
    Scaffold(
        // The `topBar` is the blue bar at the very top of the screen.
        topBar = { ModulesTopBar(navController) },
        // This sets the background color of the main content area.
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->

        // `Column` arranges things vertically, one after another.
        Column(
            // A `Modifier` is used to decorate or change a Composable.
            modifier = Modifier
                // This padding prevents our content from hiding behind the top bar.
                .padding(paddingValues)
                // This makes the Column fill the entire available screen space.
                .fillMaxSize()
                // This adds 16dp of space on the left and right sides of the screen.
                .padding(horizontal = 16.dp)
        ) {
            // --- 3. ADD THE UI ELEMENTS TO THE SCREEN ---
            // We call our other Composable functions here in the order we want them to appear.

            // Add some empty space at the top.
            Spacer(modifier = Modifier.height(16.dp))

            // Add the search bar.
            ModulesSearchBar()

            // Add more empty space.
            Spacer(modifier = Modifier.height(24.dp))

            // Add the 2x2 grid of category buttons.
            CategoryGrid(categoryList = categories, navController = navController)
        }
    }
}


// --- BUILDING BLOCKS FOR OUR SCREEN ---
// We create a separate function for each part of the UI to keep our code clean.

/**
 * Creates the blue bar at the top of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesTopBar(navController: androidx.navigation.NavController) {
    TopAppBar(
        title = { Text("Indian Sigin Language ", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        // This sets the colors for the top bar.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4A90E2), // Blue background
            titleContentColor = Color.White,     // White text
            navigationIconContentColor = Color.White // White back arrow
        )
    )
}

/**
 * Creates the search input field.
 */
@Composable
fun ModulesSearchBar() {
    // `remember` and `mutableStateOf` create a piece of memory for our Composable.
    // It holds the text that the user types. When the text changes, the UI will update automatically.
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { newText -> searchText = newText }, // Update our memory when the user types.
        placeholder = { Text("Search for a sign...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        modifier = Modifier.fillMaxWidth(), // Make the search bar stretch to the full width.
        shape = RoundedCornerShape(30.dp), // Makes the corners very round.
        // We change the colors to remove the default underline.
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

/**
 * Creates the 2x2 grid of category buttons.
 */
@Composable
fun CategoryGrid(categoryList: List<SignCategory>, navController: androidx.navigation.NavController) {
    // `LazyVerticalGrid` is a smart grid. It only shows the items that are visible on screen,
    // which is very efficient if you have many items.
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // We want exactly 2 columns.
        verticalArrangement = Arrangement.spacedBy(16.dp), // 16dp of space between the rows.
        horizontalArrangement = Arrangement.spacedBy(16.dp) // 16dp of space between the columns.
    ) {
        // This tells the grid to create a `CategoryCard` for each item in our `categoryList`.
        items(categoryList) { category ->
            CategoryCard(category = category) {
                when (category.title) {
                    "Numbers" -> navController.navigate("numbers")
                    "Greetings" -> navController.navigate("greetings")
                    "Common Words" -> navController.navigate("greetings") // Use greetings for now
                    "Alphabets" -> navController.navigate("greetings") // Use greetings for now
                }
            }
        }
    }
}

/**
 * Creates a single white, square card for a category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: SignCategory, onClick: () -> Unit) {
    // `Card` gives us a nice-looking container with a shadow and rounded corners.
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f), // This forces the card to be a perfect square.
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // We use a Column to stack the icon and text vertically inside the card.
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally, // Center everything horizontally.
            verticalArrangement = Arrangement.Center           // Center everything vertically.
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp)) // Space between icon and text.
            Text(
                text = category.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}


// --- PREVIEW ---
// The `@Preview` annotation allows us to see our Composable in the design view of Android Studio
// without needing to run the app on a phone. It's great for quick previews.
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ISLConnectScreenPreview() {
    Modules(navController = androidx.navigation.compose.rememberNavController())
}

