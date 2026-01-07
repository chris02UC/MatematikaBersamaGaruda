package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matematikabersamagaruda.viewmodel.ZenViewModel
import kotlin.math.min

@Composable
fun ZenDrawPage(
    navController: NavController,
    isHard: Boolean,
    vm: ZenViewModel = viewModel()
) {
    val state = vm.drawUiState
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    // Auto-submit logic: monitors AI recognizedDigits and current question answer
    LaunchedEffect(state.recognizedDigits, vm.currentQuestion) {
        val input = state.recognizedDigits
        val answer = vm.currentQuestion.answer
        if (input.isNotEmpty() && input.toIntOrNull() == answer) {
            vm.nextQuestion(isHard) // Increments score and resets board
        }
    }

    LaunchedEffect(Unit) {
        vm.startZenMode(isHard)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEDF2F4) // New background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Row: Back Button (from old UI style)
            Box(
                Modifier
                    .align(Alignment.Start)
                    .size(50.dp)
                    .background(Color(0xFF2B2D42), RoundedCornerShape(12.dp))
                    .clickable { navController.navigate("zenresults/${vm.correctCount}") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // 2. Middle Counter: Replaces Timer from old UI
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Solved",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B2D42).copy(alpha = 0.6f)
                )
                Text(
                    "${vm.correctCount}",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2B2D42)
                )
            }

            Spacer(Modifier.height(20.dp))

            // 3. Question text (Scaled down to 50sp for 4-digit fit)
            Text(
                text = vm.currentQuestion.displayText(),
                fontSize = 50.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2B2D42)
            )

            Spacer(Modifier.height(16.dp))

            // 4. Main Card (Replicating VersusViewDraw layout)
            Card(
                Modifier.fillMaxWidth().fillMaxHeight(0.9f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF8D99AE)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    Modifier.fillMaxSize().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Recognized digits display bar
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(Color(0xFF2B2D42), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = state.recognizedDigits,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(end = 20.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Decorative separator from old code
                    Canvas(Modifier.fillMaxWidth().height(16.dp)) {
                        val stroke = 2.dp.toPx()
                        val r = size.height / 2
                        val w = size.width
                        drawArc(Color.LightGray, 180f, 90f, false, topLeft = Offset(0f, r), size = Size(2*r, 2*r), style = Stroke(stroke))
                        drawLine(Color.LightGray, Offset(r, r), Offset(w-r, r), strokeWidth = stroke)
                        drawArc(Color.LightGray, 270f, 90f, false, topLeft = Offset(w-2*r, r), size = Size(2*r, 2*r), style = Stroke(stroke))
                    }

                    Spacer(Modifier.height(8.dp))

                    // Black Input Pad
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .onSizeChanged { canvasSize = it }
                            .background(Color.Black, RoundedCornerShape(16.dp))
                            .pointerInput(canvasSize) {
                                detectDragGestures(
                                    onDragStart = { p ->
                                        if (canvasSize.width > 0) {
                                            vm.startStroke(p.x / canvasSize.width * 500f, p.y / canvasSize.height * 500f)
                                        }
                                    },
                                    onDrag = { ch, _ ->
                                        vm.addPoint(ch.position.x / canvasSize.width * 500f, ch.position.y / canvasSize.height * 500f)
                                    },
                                    onDragEnd = { vm.endStroke() }
                                )
                            }
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            if (canvasSize.width > 0) {
                                val sx = size.width / 500f
                                val sy = size.height / 500f
                                val sw = 16f * min(sx, sy)
                                state.allStrokes.forEach { s ->
                                    drawPath(createPath(s, sx, sy), Color.White, style = Stroke(sw))
                                }
                                drawPath(createPath(state.currentStroke, sx, sy), Color.White, style = Stroke(sw))
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // Red Clear Button
                    Button(
                        onClick = { vm.clearCanvas() },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CLEAR", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

private fun createPath(points: List<Pair<Float, Float>>, sx: Float, sy: Float): Path {
    val path = Path()
    if (points.isNotEmpty()) {
        path.moveTo(points[0].first * sx, points[0].second * sy)
        points.forEach { path.lineTo(it.first * sx, it.second * sy) }
    }
    return path
}