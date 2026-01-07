package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.matematikabersamagaruda.R

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEDF2F4) // Global background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Garuda Image
            Image(
                painter = painterResource(id = R.drawable.garuda),
                contentDescription = "Garuda Bird",
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title Text
            Text(
                text = "Matematika\nBersama Garuda",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2B2D42),
                textAlign = TextAlign.Center,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quiz Mode Button (Large Red)
            // Aspect Ratio 1.5 matches 360/240 from Figma
            Button(
                onClick = { navController.navigate("quizmodestart") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1.8f), // Adjusted slightly from 1.5 to ensure it fits vertically with the other button
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Quiz Mode",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Zen Mode Button (Large Black)
            Button(
                onClick = { navController.navigate("zenmodestart") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1.8f),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2D42)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Zen Mode",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}