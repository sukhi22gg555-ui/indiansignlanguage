// In Sign.kt
package com.example.indiansignlanguage

import androidx.annotation.DrawableRes

data class Sign(
    val character: String,
    val description: String,
    @DrawableRes val imageResId: Int // This will hold the reference to our image
)