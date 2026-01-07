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
    val percentage = if (total > 0) (right.toFloat() / total.toFloat() * 100).toInt() else 0

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEDF2F4)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Quiz Result", fontSize = 40.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            Text("$percentage%", fontSize = 80.sp, fontWeight = FontWeight.Black, color = Color(0xFFD90429))

            Text("You got $right out of $total correct!", fontSize = 24.sp)

            Spacer(Modifier.height(48.dp))

            Button(onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }) {
                Text("Back to Menu")
            }
        }
    }
}