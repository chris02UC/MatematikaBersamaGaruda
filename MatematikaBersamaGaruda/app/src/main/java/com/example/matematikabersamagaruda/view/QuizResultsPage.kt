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
fun QuizResultsPage(navController: NavController, right: Int, total: Int) {
    val percentage = if (total > 0) (right.toFloat() / total.toFloat() * 100) else 0f

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEDF2F4)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF2B2D42)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Results", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    Spacer(Modifier.height(24.dp))

                    Text("${String.format("%.1f", percentage)}%", fontSize = 64.sp, fontWeight = FontWeight.Black, color = Color.White)

                    Spacer(Modifier.height(16.dp))

                    Text("$right out of $total", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("questions answered correctly", fontSize = 16.sp, color = Color.White)

                    Spacer(Modifier.height(48.dp))

                    Button(
                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Main Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}