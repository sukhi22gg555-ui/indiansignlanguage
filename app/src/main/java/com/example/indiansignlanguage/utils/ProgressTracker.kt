package com.example.indiansignlanguage.utils

import android.util.Log
import com.example.indiansignlanguage.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

/**
 * Progress tracking utility for managing user learning progress
 */
object ProgressTracker {
    private const val TAG = "ProgressTracker"
    
    /**
     * Record lesson completion with score
     */
    suspend fun recordLessonCompletion(
        lessonId: String,
        lessonName: String,
        moduleType: String,
        score: Int,
        maxScore: Int = 100,
        timeSpentMinutes: Int = 0
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val lessonProgress = LessonProgress(
                    lessonId = lessonId,
                    lessonName = lessonName,
                    moduleType = moduleType,
                    completedAt = System.currentTimeMillis(),
                    score = score,
                    maxScore = maxScore,
                    attemptsCount = 1,
                    timeSpentMinutes = timeSpentMinutes
                )
                
                // Save lesson progress
                val saveResult = FirebaseUtils.saveLessonProgress(lessonProgress)
                
                if (saveResult.isSuccess) {
                    // Update module progress
                    FirebaseUtils.updateModuleProgress(moduleType, getModuleDisplayName(moduleType))
                    
                    // Check for achievements
                    FirebaseUtils.checkAndAwardAchievements()
                    
                    // Update daily stats
                    updateDailyStats(score, timeSpentMinutes)
                    
                    Log.d(TAG, "Lesson completion recorded successfully: $lessonId")
                }
                
                saveResult
            } catch (e: Exception) {
                Log.e(TAG, "Error recording lesson completion", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Record quiz completion with detailed scoring
     */
    suspend fun recordQuizCompletion(
        quizId: String,
        moduleType: String,
        totalQuestions: Int,
        correctAnswers: Int,
        timeSpentMinutes: Int,
        questionsData: List<QuestionResult>
    ): Result<QuizResult> {
        return withContext(Dispatchers.IO) {
            try {
                val score = ((correctAnswers.toFloat() / totalQuestions.toFloat()) * 100).toInt()
                val quizResult = QuizResult(
                    quizId = quizId,
                    moduleType = moduleType,
                    totalQuestions = totalQuestions,
                    correctAnswers = correctAnswers,
                    score = score,
                    timeSpentMinutes = timeSpentMinutes,
                    completedAt = System.currentTimeMillis(),
                    questionsData = questionsData
                )
                
                // Save quiz result
                val userId = FirebaseUtils.getCurrentUserId()
                if (userId != null) {
                    FirebaseUtils.firestore.collection("users")
                        .document(userId)
                        .collection("quizResults")
                        .document(quizId)
                        .set(quizResult)
                        .await()
                    
                    // Also record as lesson completion
                    recordLessonCompletion(
                        lessonId = "quiz_$quizId",
                        lessonName = "Quiz: ${getModuleDisplayName(moduleType)}",
                        moduleType = moduleType,
                        score = score,
                        timeSpentMinutes = timeSpentMinutes
                    )
                    
                    Log.d(TAG, "Quiz completion recorded: $quizId, Score: $score%")
                    Result.success(quizResult)
                } else {
                    Result.failure(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error recording quiz completion", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Update daily learning statistics
     */
    private suspend fun updateDailyStats(score: Int, timeSpentMinutes: Int) {
        try {
            val today = FirebaseUtils.getTodayDateString()
            
            // Get existing stats for today or create new
            val currentStats = FirebaseUtils.getLearningStats(today, today)
                .getOrNull()?.firstOrNull()
                ?: LearningStats(date = today)
            
            val updatedStats = currentStats.copy(
                lessonsCompleted = currentStats.lessonsCompleted + 1,
                totalScore = currentStats.totalScore + score,
                timeSpentMinutes = currentStats.timeSpentMinutes + timeSpentMinutes,
                streakDays = calculateStreak() // Calculate current streak
            )
            
            FirebaseUtils.saveDailyStats(updatedStats)
            
            Log.d(TAG, "Daily stats updated for $today")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating daily stats", e)
        }
    }
    
    /**
     * Calculate current learning streak
     */
    private suspend fun calculateStreak(): Int {
        return try {
            val endDate = FirebaseUtils.getTodayDateString()
            val startDate = getDateDaysAgo(30) // Check last 30 days
            
            val stats = FirebaseUtils.getLearningStats(startDate, endDate)
                .getOrNull() ?: emptyList()
            
            // Sort by date descending and count consecutive days with lessons
            val sortedStats = stats.sortedByDescending { it.date }
            var streak = 0
            
            for (stat in sortedStats) {
                if (stat.lessonsCompleted > 0) {
                    streak++
                } else {
                    break
                }
            }
            
            streak
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating streak", e)
            0
        }
    }
    
    /**
     * Get user's module completion status
     */
    suspend fun getModuleCompletionStatus(): Result<Map<String, ModuleProgress>> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = FirebaseUtils.getCurrentUserId()
                if (userId != null) {
                    val querySnapshot = FirebaseUtils.firestore.collection("users")
                        .document(userId)
                        .collection("moduleProgress")
                        .get()
                        .await()
                    
                    val moduleProgressMap = querySnapshot.documents.associate { doc ->
                        val progress = doc.toObject(ModuleProgress::class.java)
                        doc.id to (progress ?: ModuleProgress())
                    }
                    
                    Result.success(moduleProgressMap)
                } else {
                    Result.failure(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting module completion status", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get user's best scores for each module
     */
    suspend fun getBestScores(): Result<Map<String, Int>> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = FirebaseUtils.getCurrentUserId()
                if (userId != null) {
                    val querySnapshot = FirebaseUtils.firestore.collection("users")
                        .document(userId)
                        .collection("lessonProgress")
                        .get()
                        .await()
                    
                    val bestScores = querySnapshot.documents
                        .mapNotNull { doc -> doc.toObject(LessonProgress::class.java) }
                        .groupBy { it.moduleType }
                        .mapValues { (_, lessons) ->
                            lessons.maxOfOrNull { it.score } ?: 0
                        }
                    
                    Result.success(bestScores)
                } else {
                    Result.failure(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting best scores", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get recent learning activity
     */
    suspend fun getRecentActivity(limit: Int = 10): Result<List<LessonProgress>> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = FirebaseUtils.getCurrentUserId()
                if (userId != null) {
                    val querySnapshot = FirebaseUtils.firestore.collection("users")
                        .document(userId)
                        .collection("lessonProgress")
                        .orderBy("completedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                        .limit(limit.toLong())
                        .get()
                        .await()
                    
                    val recentActivity = querySnapshot.documents
                        .mapNotNull { doc -> doc.toObject(LessonProgress::class.java) }
                    
                    Result.success(recentActivity)
                } else {
                    Result.failure(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting recent activity", e)
                Result.failure(e)
            }
        }
    }
    
    /**
     * Helper functions
     */
    private fun getModuleDisplayName(moduleType: String): String {
        return when (moduleType.lowercase()) {
            "alphabets" -> "Alphabets"
            "numbers" -> "Numbers"
            "greetings" -> "Greetings"
            "commonwords" -> "Common Words"
            else -> moduleType.replaceFirstChar { it.uppercase() }
        }
    }
    
    private fun getDateDaysAgo(days: Int): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -days)
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(calendar.time)
    }
}

/**
 * Data classes for quiz tracking
 */
data class QuizResult(
    val quizId: String = "",
    val moduleType: String = "",
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val score: Int = 0,
    val timeSpentMinutes: Int = 0,
    val completedAt: Long = 0L,
    val questionsData: List<QuestionResult> = emptyList()
)

data class QuestionResult(
    val questionId: String = "",
    val question: String = "",
    val userAnswer: String = "",
    val correctAnswer: String = "",
    val isCorrect: Boolean = false,
    val timeSpentSeconds: Int = 0
)