package com.example.matematikabersamagaruda.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.matematikabersamagaruda.viewmodel.QuizViewModel
import kotlin.math.min

@Composable
fun QuizDrawPage(
    navController: NavController,
    isHard: Boolean,
    time: Int,
    totalQuestions: Int,
    vm: QuizViewModel = viewModel()
) {
    val state = vm.drawUiState
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(Unit) {
        vm.startQuiz(isHard, time, totalQuestions)
    }

    LaunchedEffect(vm.isGameOver) {
        if (vm.isGameOver) {
            navController.navigate("quizresults/${vm.questionsRight}/$totalQuestions")
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEDF2F4)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Countdown Timer Display
            Text("Time Left", fontSize = 18.sp, color = Color(0xFF2B2D42).copy(alpha = 0.6f))
            Text(
                "${vm.timeLeft}s",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFD90429)
            )

            Spacer(Modifier.height(16.dp))

            // 2. Question Text
            Text(
                text = vm.currentQuestion.displayText(),
                fontSize = 50.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2B2D42)
            )

            Spacer(Modifier.height(12.dp))

            // 3. Inner UI Container
            Card(
                Modifier.fillMaxWidth(), // Removed fillMaxHeight(0.95f) to let it wrap content naturally
                colors = CardDefaults.cardColors(containerColor = Color(0xFF8D99AE)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    Modifier.padding(12.dp), // Removed fillMaxSize() to let it wrap content
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Recognized Input Bar
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

                    // Decorative separator line
                    Canvas(Modifier.fillMaxWidth().height(16.dp)) {
                        val stroke = 2.dp.toPx()
                        val r = size.height / 2
                        val w = size.width
                        drawArc(Color(0xFF2B2D42), 180f, 90f, false, topLeft = Offset(0f, r), size = Size(2*r, 2*r), style = Stroke(stroke))
                        drawLine(Color(0xFF2B2D42), Offset(r, r), Offset(w-r, r), strokeWidth = stroke)
                        drawArc(Color(0xFF2B2D42), 270f, 90f, false, topLeft = Offset(w-2*r, r), size = Size(2*r, 2*r), style = Stroke(stroke))
                    }

                    Spacer(Modifier.height(8.dp))

                    // 4. Black Drawing Pad (User Input Box)
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
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            if (canvasSize.width > 0) {
                                val sx = size.width / 500f
                                val sy = size.height / 500f
                                val strokeWidth = 16f * min(sx, sy)

                                state.allStrokes.forEach { points ->
                                    drawPath(
                                        path = createPath(points, sx, sy),
                                        color = Color.White,
                                        style = Stroke(width = strokeWidth)
                                    )
                                }
                                drawPath(
                                    path = createPath(state.currentStroke, sx, sy),
                                    color = Color.White,
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                        }
                    }

                    // --- THE FIX IS HERE ---
                    // Replaced Spacer(Modifier.weight(1f)) with a fixed height spacer
                    Spacer(Modifier.height(24.dp))

                    // 5. Side-by-Side Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { vm.clearCanvas() },
                            modifier = Modifier.weight(1f).height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2D42)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("CLEAR", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { vm.submitAnswer(isHard, totalQuestions) },
                            modifier = Modifier.weight(1f).height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD90429)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("SUBMIT", fontWeight = FontWeight.Bold)
                        }
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