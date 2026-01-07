package com.example.matematikabersamagaruda.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matematikabersamagaruda.R
import com.example.matematikabersamagaruda.view.MenuActionButton
import com.example.matematikabersamagaruda.ui.theme.*

@Composable
fun MathMenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.garuda),
            contentDescription = "Garuda Logo",
            modifier = Modifier.height(150.dp).fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Matematika\nBersama Garuda",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        MenuActionButton(
            text = "Quiz Mode",
            backgroundColor = QuizButtonColor,
            onClick = { /* Navigate to Quiz */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuActionButton(
            text = "Zen Mode",
            backgroundColor = ZenButtonColor,
            onClick = { /* Navigate to Zen */ }
        )
    }
}