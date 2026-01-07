package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ZenResultsPage(navController: NavController, score: Int) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEDF2F4)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Zen Mode Finished",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2D42)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Score Display Box
            Card(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2D42)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Total Answered:", color = Color.White.copy(alpha = 0.7f), fontSize = 20.sp)
                    Text("$score", color = Color.White, fontSize = 60.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Back to Main Menu Button
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Main Menu", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}