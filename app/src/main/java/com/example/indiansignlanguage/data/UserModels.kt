package com.example.indiansignlanguage.data

/**
 * User profile data stored in Firestore
 */
data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val createdAt: Long = 0L,
    val totalLessonsCompleted: Int = 0,
    val currentLevel: Int = 1,
    val totalScore: Int = 0,
    val lastActiveDate: Long = 0L,
    val completedModules: List<String> = emptyList(),
    val achievements: List<String> = emptyList()
)

/**
 * Individual lesson progress tracking
 */
data class LessonProgress(
    val lessonId: String = "",
    val lessonName: String = "",
    val moduleType: String = "", // "numbers", "greetings", "alphabets", etc.
    val completedAt: Long = 0L,
    val score: Int = 0,
    val maxScore: Int = 100,
    val attemptsCount: Int = 0,
    val timeSpentMinutes: Int = 0
)

/**
 * Module progress (like Numbers, Greetings, etc.)
 */
data class ModuleProgress(
    val moduleId: String = "",
    val moduleName: String = "",
    val totalLessons: Int = 0,
    val completedLessons: Int = 0,
    val averageScore: Double = 0.0,
    val isCompleted: Boolean = false,
    val completedAt: Long = 0L,
    val totalTimeSpent: Int = 0 // in minutes
)

/**
 * Daily/Weekly learning statistics
 */
data class LearningStats(
    val date: String = "", // Format: "2025-09-27"
    val lessonsCompleted: Int = 0,
    val totalScore: Int = 0,
    val timeSpentMinutes: Int = 0,
    val streakDays: Int = 0
)

/**
 * Achievement system
 */
data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val iconName: String = "",
    val unlockedAt: Long = 0L,
    val requirement: String = "" // e.g., "Complete 10 lessons", "Score 80% in a module"
)