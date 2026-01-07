package com.example.matematikabersamagaruda.view // Replace with your actual package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matematikabersamagaruda.R

// --- Constants for Colors derived from the image ---
val BackgroundColor = Color(0xFFEEF1F4) // Light Grey background
val QuizButtonColor = Color(0xFFD90025) // Vibrant Red
val ZenButtonColor = Color(0xFF303446)  // Dark slate blue/grey

// --- Main Activity (for running on device) ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MathMenuScreen()
                }
            }
        }
    }
}

// --- Composable Implementation ---

@Composable
fun MathMenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            // Add padding around the entire screen content
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Distribute space evenly, pushing content towards center
        verticalArrangement = Arrangement.Center
    ) {

        // 1. The Top Image (Garuda)
        // IMPORTANT: Ensure you have a drawable resource named 'garuda'
        // If you don't have it yet, use placeholder: R.drawable.ic_launcher_foreground
        Image(
            painter = painterResource(id = R.drawable.garuda),
            contentDescription = "Garuda Pancasila Emblem",
            modifier = Modifier
                .height(150.dp) // Adjust height as needed to match aspect ratio
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. The Title Text
        Text(
            text = "Matematika\nBersama Garuda",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp // Adjust line height for the multi-line text
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 3. Quiz Mode Button
        MenuActionButton(
            text = "Quiz Mode",
            backgroundColor = QuizButtonColor,
            onClick = { /* Handle Quiz Mode Click */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Zen Mode Button
        MenuActionButton(
            text = "Zen Mode",
            backgroundColor = ZenButtonColor,
            onClick = { /* Handle Zen Mode Click */ }
        )
    }
}

// --- Reusable Button Component ---
// Since both buttons look the same except for color and text, we create a reusable composable.
@Composable
fun MenuActionButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp), // The buttons in the image are quite tall
        shape = RoundedCornerShape(24.dp), // Highly rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        )
    }
}

// --- Preview for Android Studio ---
// Note: The preview might fail to render the image if R.drawable.garuda is missing.
// Replace it with a standard icon resource for preview purposes if needed.
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun MathMenuScreenPreview() {
    MaterialTheme {
        MathMenuScreen()
    }
}

