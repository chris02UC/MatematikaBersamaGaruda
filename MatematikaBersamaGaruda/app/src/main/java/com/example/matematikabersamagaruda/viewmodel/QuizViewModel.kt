package com.example.matematikabersamagaruda.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.example.matematikabersamagaruda.model.Question
import com.example.matematikabersamagaruda.model.DrawUiState

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val drawViewModel = DrawViewModel(application)

    var currentQuestion by mutableStateOf(Question.generate(false))
    var timeLeft by mutableStateOf(0)
    var questionsRight by mutableStateOf(0)
    var questionsAttempted by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)

    val drawUiState: DrawUiState get() = drawViewModel.uiState

    private var timer: CountDownTimer? = null

    fun startQuiz(isHard: Boolean, seconds: Int, totalQuestions: Int) {
        currentQuestion = Question.generate(isHard)
        timeLeft = seconds
        questionsRight = 0
        questionsAttempted = 0
        isGameOver = false

        timer?.cancel()
        timer = object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
            }
            override fun onFinish() {
                isGameOver = true
            }
        }.start()
    }

    fun submitAnswer(isHard: Boolean, totalQuestions: Int) {
        val recognized = drawViewModel.uiState.recognizedDigits
        if (recognized.isNotEmpty() && recognized.toIntOrNull() == currentQuestion.answer) {
            questionsRight++
        }

        questionsAttempted++

        if (questionsAttempted >= totalQuestions) {
            timer?.cancel()
            isGameOver = true
        } else {
            currentQuestion = Question.generate(isHard)
            drawViewModel.clearCanvas()
        }
    }

    // Pass-through functions for the drawing pad
    fun startStroke(x: Float, y: Float) = drawViewModel.startStroke(x, y)
    fun addPoint(x: Float, y: Float) = drawViewModel.addPointToStroke(x, y)
    fun endStroke() = drawViewModel.endStroke()
    fun clearCanvas() = drawViewModel.clearCanvas()
}