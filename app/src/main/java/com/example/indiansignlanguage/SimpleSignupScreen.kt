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
import com.google.firebase.ktx.Firebase
import com.example.indiansignlanguage.data.UserProfile
import com.example.indiansignlanguage.utils.FirebaseUtils
import kotlinx.coroutines.launch

/**
 * Simple SignUp screen that only uses Firebase Authentication
 * This is for testing purposes to isolate the auth issue
 */
@Composable
fun SimpleSignUpScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()

    // Function to handle the simple signup process
    fun handleSimpleSignUp() {
        // Basic validation
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill in email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        Toast.makeText(context, "Creating account...", Toast.LENGTH_SHORT).show()

        // Firebase Authentication with user profile creation
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // Create user profile in Firestore
                val userProfile = UserProfile(
                    fullName = if (fullName.isNotBlank()) fullName else "User",
                    email = email,
                    createdAt = System.currentTimeMillis(),
                    totalLessonsCompleted = 0,
                    currentLevel = 1,
                    totalScore = 0,
                    lastActiveDate = System.currentTimeMillis(),
                    completedModules = emptyList(),
                    achievements = emptyList()
                )
                
                // Save user profile to Firestore
                coroutineScope.launch {
                    FirebaseUtils.saveUserProfile(userProfile).fold(
                        onSuccess = {
                            isLoading = false
                            Toast.makeText(context, "Account created successfully! You can now login.", Toast.LENGTH_LONG).show()
                            
                            // Navigate to login immediately
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onFailure = { error ->
                            isLoading = false
                            Toast.makeText(context, "Account created but profile setup failed: ${error.message}", Toast.LENGTH_LONG).show()
                            
                            // Still navigate to login
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
            .addOnFailureListener { exception ->
                isLoading = false
                Toast.makeText(context, "Failed: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
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
            text = "Create Account (Simple)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name (Optional)") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full Name Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
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
            shape = RoundedCornerShape(8.dp)
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
            shape = RoundedCornerShape(8.dp)
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
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { handleSimpleSignUp() },
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
                Text("Create Account (Auth Only)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
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