package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun QuizModeStart(navController: NavController) {
    var timerInput by remember { mutableStateOf("60") }
    var questionCountInput by remember { mutableStateOf("10") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEDF2F4)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Quiz Mode Setup", fontSize = 32.sp, color = Color(0xFF2B2D42))

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = timerInput,
                onValueChange = { timerInput = it },
                label = { Text("Timer (seconds)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = questionCountInput,
                onValueChange = { questionCountInput = it },
                label = { Text("Number of Questions") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = { navController.navigate("quizdrawpage/false/$timerInput/$questionCountInput") },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2D42))
            ) {
                Text("Easy Quiz", fontSize = 24.sp)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("quizdrawpage/true/$timerInput/$questionCountInput") },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429))
            ) {
                Text("Hard Quiz", fontSize = 24.sp)
            }
        }
    }
}