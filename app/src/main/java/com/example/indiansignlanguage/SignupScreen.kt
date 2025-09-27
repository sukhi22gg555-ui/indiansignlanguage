package com.example.indiansignlanguage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val auth = Firebase.auth
    val firestore = Firebase.firestore

    // Function to handle the signup process
    fun handleSignUp() {
        // Validation
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        Toast.makeText(context, "Creating account...", Toast.LENGTH_SHORT).show()

        // Create Firebase Authentication account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Toast.makeText(context, "Authentication successful!", Toast.LENGTH_SHORT).show()
                
                // Authentication successful, now save user profile data
                val userId = authResult.user?.uid
                if (userId != null) {
                    val userProfile = hashMapOf(
                        "fullName" to fullName,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis(),
                        "totalLessonsCompleted" to 0,
                        "currentLevel" to 1,
                        "totalScore" to 0,
                        "lastActiveDate" to System.currentTimeMillis(),
                        "completedModules" to emptyList<String>(),
                        "achievements" to emptyList<String>()
                    )

                    firestore.collection("users").document(userId)
                        .set(userProfile)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(context, "Sign Up Successful! Please login.", Toast.LENGTH_LONG).show()

                            // Navigate to login screen
                            try {
                                navController.navigate("login") {
                                    popUpTo("signup") { inclusive = true }
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { firestoreException ->
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Profile save failed: ${firestoreException.localizedMessage}. But account was created, you can login.",
                                Toast.LENGTH_LONG
                            ).show()

                            // Still navigate to login since auth was successful
                            try {
                                navController.navigate("login") {
                                    popUpTo("signup") { inclusive = true }
                                    launchSingleTop = true
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    isLoading = false
                    Toast.makeText(context, "Account creation failed - no user ID", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { authException ->
                // Authentication failed
                isLoading = false
                val errorMessage = when (authException.message) {
                    "The email address is already in use by another account." -> "This email is already registered. Try logging in instead."
                    "The email address is badly formatted." -> "Please enter a valid email address."
                    "The password is invalid or the user does not have a password." -> "Password must be at least 6 characters."
                    "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Network error. Please check your internet connection."
                    else -> "Sign Up Failed: ${authException.localizedMessage}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full Name Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4285F4),
                unfocusedBorderColor = Color(0xFFCCCCCC),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4285F4),
                unfocusedBorderColor = Color(0xFFCCCCCC),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4285F4),
                unfocusedBorderColor = Color(0xFFCCCCCC),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4285F4),
                unfocusedBorderColor = Color(0xFFCCCCCC),
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { handleSignUp() },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Sign Up", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                color = Color(0xFF666666),
                fontSize = 16.sp
            )
            TextButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            ) {
                Text(
                    text = "Login",
                    color = Color(0xFF4285F4),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}