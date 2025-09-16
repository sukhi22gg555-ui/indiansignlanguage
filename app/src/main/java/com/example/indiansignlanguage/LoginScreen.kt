package com.example.indiansignlanguage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indiansignlanguage.ui.theme.IndianSignLanguageTheme

@Composable
fun LoginScreen() {
    // We use mutableStateOf to have Compose remember what the user types.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Column arranges UI elements vertically.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp), // Add padding on the left and right
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- App Logo ---
        // Note: You need to add your logo image to the `res/drawable` folder.
        // I am using the default launcher icon as a placeholder.
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(text = "ISL Connect", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(48.dp))

        // --- Email Text Field ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Password Text Field ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(), // Hides the password
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Login Button ---
        Button(
            onClick = { /* TODO: Handle login click */ },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("or")

        Spacer(modifier = Modifier.height(24.dp))

        // --- Social Login Buttons ---
        // Note: For real icons, you would add Google/Facebook SVGs to your project.
        OutlinedButton(
            onClick = { /* TODO: Handle Google login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue with Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { /* TODO: Handle Facebook login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue with Facebook")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Sign Up Navigation ---
        Row {
            Text("Don't have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { /* TODO: Navigate to Sign Up screen */ }) {
                Text("Sign Up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    IndianSignLanguageTheme {
        LoginScreen()
    }
}