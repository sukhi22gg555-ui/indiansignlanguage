package com.example.indiansignlanguage.utils

import android.util.Log
import com.example.indiansignlanguage.data.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

object FirebaseUtils {
    private val auth = Firebase.auth
    val firestore = Firebase.firestore
    private const val TAG = "FirebaseUtils"

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Save or update user profile
     */
    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            firestore.collection("users")
                .document(userId)
                .set(userProfile)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile", e)
            Result.failure(e)
        }
    }

    /**
     * Get user profile
     */
    suspend fun getUserProfile(): Result<UserProfile> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            val userProfile = document.toObject(UserProfile::class.java)
                ?: UserProfile()
            Result.success(userProfile)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user profile", e)
            Result.failure(e)
        }
    }

    /**
     * Update lesson completion
     */
    suspend fun saveLessonProgress(lessonProgress: LessonProgress): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            // Save individual lesson progress
            firestore.collection("users")
                .document(userId)
                .collection("lessonProgress")
                .document(lessonProgress.lessonId)
                .set(lessonProgress)
                .await()

            // Update user's overall stats
            updateUserStats(lessonProgress)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving lesson progress", e)
            Result.failure(e)
        }
    }

    /**
     * Update user's overall statistics
     */
    private suspend fun updateUserStats(lessonProgress: LessonProgress) {
        val userId = getCurrentUserId() ?: return
        
        try {
            val userRef = firestore.collection("users").document(userId)
            
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentProfile = snapshot.toObject(UserProfile::class.java) ?: UserProfile()
                
                val updatedProfile = currentProfile.copy(
                    totalLessonsCompleted = currentProfile.totalLessonsCompleted + 1,
                    totalScore = currentProfile.totalScore + lessonProgress.score,
                    lastActiveDate = System.currentTimeMillis()
                )
                
                transaction.set(userRef, updatedProfile)
            }.await()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user stats", e)
        }
    }

    /**
     * Get user's lesson progress
     */
    suspend fun getUserLessonProgress(): Result<List<LessonProgress>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            val querySnapshot = firestore.collection("users")
                .document(userId)
                .collection("lessonProgress")
                .get()
                .await()
            
            val lessonProgressList = querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(LessonProgress::class.java)
            }
            
            Result.success(lessonProgressList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting lesson progress", e)
            Result.failure(e)
        }
    }

    /**
     * Save daily learning statistics
     */
    suspend fun saveDailyStats(stats: LearningStats): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("dailyStats")
                .document(stats.date)
                .set(stats)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving daily stats", e)
            Result.failure(e)
        }
    }

    /**
     * Get learning statistics for a date range
     */
    suspend fun getLearningStats(startDate: String, endDate: String): Result<List<LearningStats>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            val querySnapshot = firestore.collection("users")
                .document(userId)
                .collection("dailyStats")
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .get()
                .await()
            
            val statsList = querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(LearningStats::class.java)
            }
            
            Result.success(statsList)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting learning stats", e)
            Result.failure(e)
        }
    }

    /**
     * Update module progress
     */
    suspend fun updateModuleProgress(moduleId: String, moduleName: String): Result<Unit> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            // Get all lesson progress for this module
            val lessonProgressList = firestore.collection("users")
                .document(userId)
                .collection("lessonProgress")
                .whereEqualTo("moduleType", moduleId)
                .get()
                .await()
            
            val completedLessons = lessonProgressList.size()
            val averageScore = if (completedLessons > 0) {
                lessonProgressList.documents.mapNotNull { doc ->
                    doc.toObject(LessonProgress::class.java)?.score
                }.average()
            } else 0.0
            
            val moduleProgress = ModuleProgress(
                moduleId = moduleId,
                moduleName = moduleName,
                completedLessons = completedLessons,
                averageScore = averageScore,
                isCompleted = completedLessons >= 10, // Assume 10 lessons per module
                completedAt = if (completedLessons >= 10) System.currentTimeMillis() else 0L
            )
            
            firestore.collection("users")
                .document(userId)
                .collection("moduleProgress")
                .document(moduleId)
                .set(moduleProgress)
                .await()
                
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating module progress", e)
            Result.failure(e)
        }
    }

    /**
     * Get today's date in string format
     */
    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Check and award achievements
     */
    suspend fun checkAndAwardAchievements(): Result<List<Achievement>> {
        val userId = getCurrentUserId() ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            val userProfile = getUserProfile().getOrThrow()
            val newAchievements = mutableListOf<Achievement>()
            
            // Check for different achievement conditions
            if (userProfile.totalLessonsCompleted >= 5 && !userProfile.achievements.contains("first_5_lessons")) {
                val achievement = Achievement(
                    id = "first_5_lessons",
                    title = "Getting Started",
                    description = "Completed your first 5 lessons!",
                    iconName = "star",
                    unlockedAt = System.currentTimeMillis(),
                    requirement = "Complete 5 lessons"
                )
                newAchievements.add(achievement)
            }
            
            if (userProfile.totalLessonsCompleted >= 20 && !userProfile.achievements.contains("dedicated_learner")) {
                val achievement = Achievement(
                    id = "dedicated_learner",
                    title = "Dedicated Learner",
                    description = "Completed 20 lessons! Keep it up!",
                    iconName = "trophy",
                    unlockedAt = System.currentTimeMillis(),
                    requirement = "Complete 20 lessons"
                )
                newAchievements.add(achievement)
            }
            
            // Save new achievements
            if (newAchievements.isNotEmpty()) {
                val updatedAchievements = userProfile.achievements + newAchievements.map { it.id }
                val updatedProfile = userProfile.copy(achievements = updatedAchievements)
                saveUserProfile(updatedProfile)
                
                // Save individual achievement details
                newAchievements.forEach { achievement ->
                    firestore.collection("users")
                        .document(userId)
                        .collection("achievements")
                        .document(achievement.id)
                        .set(achievement)
                        .await()
                }
            }
            
            Result.success(newAchievements)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking achievements", e)
            Result.failure(e)
        }
    }
}