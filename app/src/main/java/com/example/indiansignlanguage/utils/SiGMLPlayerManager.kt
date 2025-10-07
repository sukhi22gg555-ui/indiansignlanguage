package com.example.indiansignlanguage.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SiGMLPlayerManager(private val context: Context) {

    /**
     * Build a list of candidate asset paths for a given word/phrase to maximize chances
     * of matching varied file naming conventions present in SignFiles collections.
     */
    private fun candidatePaths(word: String): List<String> {
        val base = word.lowercase().trim()
        val variants = mutableSetOf<String>()

        // Raw base
        variants.add(base)

        // Replace spaces and hyphens with underscores
        variants.add(base.replace(" ", "_"))
        variants.add(base.replace("-", "_"))
        variants.add(base.replace(Regex("[\\s-]+"), "_"))

        // Remove punctuation (keep letters, digits, spaces and underscores), then normalize spaces -> _
        val sanitized = base.replace(Regex("[^a-z0-9_ ]+"), "").trim()
        if (sanitized.isNotEmpty()) {
            variants.add(sanitized)
            variants.add(sanitized.replace(Regex("\\s+"), "_"))
        }

        // Common alternate with underscores converted back to spaces
        variants.add(base.replace("_", " "))

        // Deduplicate and map to full asset path
        return variants
            .filter { it.isNotBlank() }
            .distinct()
            .map { "signs/${it}.sigml" }
    }
    
/**
     * Load SiGML file from assets for a given word
     */
    suspend fun loadSiGMLFromAssets(word: String): String? = withContext(Dispatchers.IO) {
        val candidates = candidatePaths(word)
        for (fileName in candidates) {
            try {
                val inputStream = context.assets.open(fileName)
                val content = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()
                return@withContext content
            } catch (_: IOException) {
                // try next candidate
            }
        }
        return@withContext null
    }
    
    /**
     * Check if a word has an offline SiGML file
     */
    suspend fun hasOfflineSign(word: String): Boolean = withContext(Dispatchers.IO) {
        val candidates = candidatePaths(word)
        for (fileName in candidates) {
            try {
                context.assets.open(fileName).close()
                return@withContext true
            } catch (_: IOException) {
                // try next candidate
            }
        }
        return@withContext false
    }
    
    /**
     * Get list of available offline signs
     */
    suspend fun getAvailableOfflineSigns(): List<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val signFiles = context.assets.list("signs") ?: emptyArray()
            signFiles.map { it.removeSuffix(".sigml").replace("_", " ") }
        } catch (e: IOException) {
            emptyList()
        }
    }
    
    /**
     * Parse text and find words that have offline signs
     */
    suspend fun findAvailableSignsInText(text: String): List<String> = withContext(Dispatchers.IO) {
        val words = text.lowercase().split(" ", ",", ".", "!", "?", ";").map { it.trim() }.filter { it.isNotEmpty() }
        val availableSigns = mutableListOf<String>()
        
        for (word in words) {
            if (hasOfflineSign(word)) {
                availableSigns.add(word)
            }
        }
        
        return@withContext availableSigns
    }
}