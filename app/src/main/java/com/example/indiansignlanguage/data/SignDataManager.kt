package com.example.indiansignlanguage.data

import android.content.Context
import java.io.IOException

/**
 * Manages available SignML files and organizes them by categories
 */
class SignDataManager(private val context: Context) {
    
    // Categories for organizing signs
    enum class SignCategory {
        NUMBERS, ALPHABETS, GREETINGS, COMMON_WORDS, COLORS, FAMILY, 
        ACTIONS, OBJECTS, PLACES, TIME, EMOTIONS, HEALTH, TRANSPORT, WEATHER
    }
    
    // Data class for sign information
    data class SignInfo(
        val word: String,
        val signId: String,
        val category: SignCategory,
        val description: String,
        val isAvailable: Boolean = true
    )
    
    // Comprehensive sign database based on available SignML files
    private val signDatabase = mapOf(
        // Numbers (0-100)
        SignCategory.NUMBERS to (0..100).map { num ->
            SignInfo("$num", "$num", SignCategory.NUMBERS, "Number $num")
        },
        
        // Alphabets (A-Z)
        SignCategory.ALPHABETS to ('A'..'Z').map { letter ->
            SignInfo("$letter", "${letter.code}", SignCategory.ALPHABETS, "Letter $letter")
        },
        
        // Common Greetings
        SignCategory.GREETINGS to listOf(
            SignInfo("Hello", "hello", SignCategory.GREETINGS, "Basic greeting"),
            SignInfo("Thank You", "thankyou", SignCategory.GREETINGS, "Expression of gratitude"),
            SignInfo("Please", "please", SignCategory.GREETINGS, "Polite request"),
            SignInfo("Sorry", "sorry", SignCategory.GREETINGS, "Apology sign"),
            SignInfo("Welcome", "welcome", SignCategory.GREETINGS, "Welcome greeting"),
            SignInfo("Good Morning", "good_morning", SignCategory.GREETINGS, "Morning greeting"),
            SignInfo("Good Evening", "good_evening", SignCategory.GREETINGS, "Evening greeting"),
            SignInfo("Good Night", "good_night", SignCategory.GREETINGS, "Night greeting"),
            SignInfo("Goodbye", "goodbye", SignCategory.GREETINGS, "Farewell greeting"),
            SignInfo("See You Later", "see_you_later", SignCategory.GREETINGS, "Informal farewell")
        ),
        
        // Family & People
        SignCategory.FAMILY to listOf(
            SignInfo("Mother", "mother", SignCategory.FAMILY, "Female parent"),
            SignInfo("Father", "father", SignCategory.FAMILY, "Male parent"),
            SignInfo("Brother", "brother", SignCategory.FAMILY, "Male sibling"),
            SignInfo("Sister", "sister", SignCategory.FAMILY, "Female sibling"),
            SignInfo("Child", "child", SignCategory.FAMILY, "Young person"),
            SignInfo("Baby", "baby", SignCategory.FAMILY, "Infant"),
            SignInfo("Boy", "boy", SignCategory.FAMILY, "Male child"),
            SignInfo("Girl", "girl", SignCategory.FAMILY, "Female child"),
            SignInfo("Man", "man", SignCategory.FAMILY, "Adult male"),
            SignInfo("Woman", "woman", SignCategory.FAMILY, "Adult female"),
            SignInfo("Friend", "friend", SignCategory.FAMILY, "Close companion"),
            SignInfo("Family", "family", SignCategory.FAMILY, "Relatives")
        ),
        
        // Colors
        SignCategory.COLORS to listOf(
            SignInfo("Red", "red", SignCategory.COLORS, "Red color"),
            SignInfo("Blue", "blue", SignCategory.COLORS, "Blue color"),
            SignInfo("Green", "green", SignCategory.COLORS, "Green color"),
            SignInfo("Yellow", "yellow", SignCategory.COLORS, "Yellow color"),
            SignInfo("Black", "black", SignCategory.COLORS, "Black color"),
            SignInfo("White", "white", SignCategory.COLORS, "White color"),
            SignInfo("Brown", "brown", SignCategory.COLORS, "Brown color"),
            SignInfo("Orange", "orange", SignCategory.COLORS, "Orange color"),
            SignInfo("Purple", "purple", SignCategory.COLORS, "Purple color"),
            SignInfo("Pink", "pink", SignCategory.COLORS, "Pink color")
        ),
        
        // Common Actions
        SignCategory.ACTIONS to listOf(
            SignInfo("Go", "go", SignCategory.ACTIONS, "Move away"),
            SignInfo("Come", "come", SignCategory.ACTIONS, "Move toward"),
            SignInfo("Stop", "stop", SignCategory.ACTIONS, "Cease movement"),
            SignInfo("Wait", "wait", SignCategory.ACTIONS, "Remain in place"),
            SignInfo("Look", "look", SignCategory.ACTIONS, "Use eyes to see"),
            SignInfo("Sit", "sit", SignCategory.ACTIONS, "Take a seat"),
            SignInfo("Stand", "stand", SignCategory.ACTIONS, "Rise to feet"),
            SignInfo("Walk", "walk", SignCategory.ACTIONS, "Move on foot"),
            SignInfo("Run", "run", SignCategory.ACTIONS, "Move quickly"),
            SignInfo("Eat", "eat", SignCategory.ACTIONS, "Consume food"),
            SignInfo("Drink", "drink", SignCategory.ACTIONS, "Consume liquid"),
            SignInfo("Sleep", "sleep", SignCategory.ACTIONS, "Rest with closed eyes"),
            SignInfo("Work", "work", SignCategory.ACTIONS, "Perform job duties"),
            SignInfo("Study", "study", SignCategory.ACTIONS, "Learn or read"),
            SignInfo("Play", "play", SignCategory.ACTIONS, "Engage in fun activity"),
            SignInfo("Help", "help", SignCategory.ACTIONS, "Provide assistance"),
            SignInfo("Give", "give", SignCategory.ACTIONS, "Present or offer"),
            SignInfo("Take", "take", SignCategory.ACTIONS, "Receive or grab"),
            SignInfo("Open", "open", SignCategory.ACTIONS, "Make accessible"),
            SignInfo("Close", "close", SignCategory.ACTIONS, "Shut or seal")
        ),
        
        // Common Objects
        SignCategory.OBJECTS to listOf(
            SignInfo("Book", "book", SignCategory.OBJECTS, "Written publication"),
            SignInfo("Phone", "phone", SignCategory.OBJECTS, "Communication device"),
            SignInfo("Car", "car", SignCategory.OBJECTS, "Motor vehicle"),
            SignInfo("House", "house", SignCategory.OBJECTS, "Residential building"),
            SignInfo("Door", "door", SignCategory.OBJECTS, "Entrance barrier"),
            SignInfo("Window", "window", SignCategory.OBJECTS, "Glass opening"),
            SignInfo("Chair", "chair", SignCategory.OBJECTS, "Seating furniture"),
            SignInfo("Table", "table", SignCategory.OBJECTS, "Flat surface furniture"),
            SignInfo("Bed", "bed", SignCategory.OBJECTS, "Sleeping furniture"),
            SignInfo("Bag", "bag", SignCategory.OBJECTS, "Carrying container"),
            SignInfo("Key", "key", SignCategory.OBJECTS, "Lock opener"),
            SignInfo("Money", "money", SignCategory.OBJECTS, "Currency"),
            SignInfo("Water", "water", SignCategory.OBJECTS, "Clear liquid"),
            SignInfo("Food", "food", SignCategory.OBJECTS, "Edible substance")
        ),
        
        // Places
        SignCategory.PLACES to listOf(
            SignInfo("Home", "home", SignCategory.PLACES, "Residence"),
            SignInfo("School", "school", SignCategory.PLACES, "Educational institution"),
            SignInfo("Hospital", "hospital", SignCategory.PLACES, "Medical facility"),
            SignInfo("Shop", "shop", SignCategory.PLACES, "Retail store"),
            SignInfo("Office", "office", SignCategory.PLACES, "Work building"),
            SignInfo("Hotel", "hotel", SignCategory.PLACES, "Accommodation facility"),
            SignInfo("Bank", "bank", SignCategory.PLACES, "Financial institution"),
            SignInfo("Restaurant", "restaurant", SignCategory.PLACES, "Eating establishment"),
            SignInfo("City", "city", SignCategory.PLACES, "Urban area"),
            SignInfo("Village", "village", SignCategory.PLACES, "Rural community")
        ),
        
        // Time-related
        SignCategory.TIME to listOf(
            SignInfo("Today", "today", SignCategory.TIME, "Current day"),
            SignInfo("Tomorrow", "tomorrow", SignCategory.TIME, "Next day"),
            SignInfo("Yesterday", "yesterday", SignCategory.TIME, "Previous day"),
            SignInfo("Morning", "morning", SignCategory.TIME, "Early day"),
            SignInfo("Afternoon", "afternoon", SignCategory.TIME, "Mid day"),
            SignInfo("Evening", "evening", SignCategory.TIME, "Late day"),
            SignInfo("Night", "night", SignCategory.TIME, "Dark hours"),
            SignInfo("Time", "time", SignCategory.TIME, "Duration or moment"),
            SignInfo("Week", "week", SignCategory.TIME, "Seven days"),
            SignInfo("Month", "month", SignCategory.TIME, "Calendar period"),
            SignInfo("Year", "year", SignCategory.TIME, "Twelve months")
        ),
        
        // Emotions
        SignCategory.EMOTIONS to listOf(
            SignInfo("Happy", "happy", SignCategory.EMOTIONS, "Joyful feeling"),
            SignInfo("Sad", "sad", SignCategory.EMOTIONS, "Sorrowful feeling"),
            SignInfo("Angry", "angry", SignCategory.EMOTIONS, "Furious feeling"),
            SignInfo("Love", "love", SignCategory.EMOTIONS, "Deep affection"),
            SignInfo("Like", "like", SignCategory.EMOTIONS, "Positive preference"),
            SignInfo("Hate", "hate", SignCategory.EMOTIONS, "Strong dislike"),
            SignInfo("Fear", "fear", SignCategory.EMOTIONS, "Afraid feeling"),
            SignInfo("Surprise", "surprise", SignCategory.EMOTIONS, "Unexpected reaction"),
            SignInfo("Excited", "excited", SignCategory.EMOTIONS, "Enthusiastic feeling")
        ),
        
        // Transportation
        SignCategory.TRANSPORT to listOf(
            SignInfo("Bus", "bus", SignCategory.TRANSPORT, "Public vehicle"),
            SignInfo("Train", "train", SignCategory.TRANSPORT, "Railway vehicle"),
            SignInfo("Airplane", "airplane", SignCategory.TRANSPORT, "Flying vehicle"),
            SignInfo("Bicycle", "bike", SignCategory.TRANSPORT, "Two-wheeled vehicle"),
            SignInfo("Motorcycle", "motorcycle", SignCategory.TRANSPORT, "Motorized bike"),
            SignInfo("Boat", "boat", SignCategory.TRANSPORT, "Water vessel"),
            SignInfo("Taxi", "taxi", SignCategory.TRANSPORT, "Hired car")
        )
    )
    
    /**
     * Get all signs for a specific category
     */
    fun getSignsForCategory(category: SignCategory): List<SignInfo> {
        return signDatabase[category] ?: emptyList()
    }
    
    /**
     * Search for signs by keyword
     */
    fun searchSigns(query: String): List<SignInfo> {
        val searchQuery = query.lowercase().trim()
        if (searchQuery.isEmpty()) return emptyList()
        
        return signDatabase.values.flatten().filter { signInfo ->
            signInfo.word.lowercase().contains(searchQuery) ||
            signInfo.description.lowercase().contains(searchQuery) ||
            signInfo.signId.lowercase().contains(searchQuery)
        }
    }
    
    /**
     * Get all available signs
     */
    fun getAllSigns(): List<SignInfo> {
        return signDatabase.values.flatten()
    }
    
    /**
     * Get sign by exact word match
     */
    fun getSignByWord(word: String): SignInfo? {
        return getAllSigns().find { it.word.lowercase() == word.lowercase() }
    }
    
    /**
     * Check if a SignML file exists for a given sign ID
     */
    fun isSignAvailable(signId: String): Boolean {
        return try {
            context.assets.open("signs/$signId.sigml").use { it.available() > 0 }
        } catch (e: IOException) {
            // Try alternative naming conventions
            try {
                context.assets.open("signs/${signId.lowercase()}.sigml").use { it.available() > 0 }
            } catch (e2: IOException) {
                false
            }
        }
    }
    
    /**
     * Get available signs from a text input
     */
    fun findAvailableSignsInText(text: String): List<String> {
        val words = text.split(" ", ",", ".", "!", "?", ";", ":")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }
        
        return words.mapNotNull { word ->
            getAllSigns().find { sign -> 
                sign.word.lowercase() == word && isSignAvailable(sign.signId)
            }?.signId
        }.distinct()
    }
    
    /**
     * Get statistics about available signs
     */
    fun getSignStatistics(): Map<String, Int> {
        val allSigns = getAllSigns()
        val availableSigns = allSigns.filter { isSignAvailable(it.signId) }
        
        return mapOf(
            "total" to allSigns.size,
            "available" to availableSigns.size,
            "numbers" to getSignsForCategory(SignCategory.NUMBERS).size,
            "alphabets" to getSignsForCategory(SignCategory.ALPHABETS).size,
            "greetings" to getSignsForCategory(SignCategory.GREETINGS).size,
            "common_words" to (allSigns.size - getSignsForCategory(SignCategory.NUMBERS).size - getSignsForCategory(SignCategory.ALPHABETS).size),
            "categories" to SignCategory.values().size
        )
    }
}