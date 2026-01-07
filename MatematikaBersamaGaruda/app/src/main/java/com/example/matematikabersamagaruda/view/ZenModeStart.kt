package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ZenModeStart(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEDF2F4)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Zen Mode", fontSize = 40.sp, color = Color(0xFF2B2D42))
            Spacer(Modifier.height(40.dp))

            // Easy Mode Button
            Button(
                onClick = { navController.navigate("zendrawpage/false") },
                modifier = Modifier.fillMaxWidth(0.8f).height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2D42))
            ) {
                Text("Easy Mode", fontSize = 24.sp)
            }

            Spacer(Modifier.height(20.dp))

            // Hard Mode Button
            Button(
                onClick = { navController.navigate("zendrawpage/true") },
                modifier = Modifier.fillMaxWidth(0.8f).height(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429))
            ) {
                Text("Hard Mode", fontSize = 24.sp)
            }
        }
    }
}