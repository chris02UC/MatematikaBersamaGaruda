package com.example.matematikabersamagaruda.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.example.matematikabersamagaruda.model.Question
import com.example.matematikabersamagaruda.model.DrawUiState

class ZenViewModel(application: Application) : AndroidViewModel(application) {

    private val drawViewModel = DrawViewModel(application)

    var currentQuestion by mutableStateOf(Question.generate(false))
        private set

    var correctCount by mutableStateOf(0)
        private set

    val drawUiState: DrawUiState
        get() = drawViewModel.uiState

    fun startZenMode(isHard: Boolean) {
        currentQuestion = Question.generate(isHard)
        correctCount = 0
        clearCanvas()
    }

    fun nextQuestion(isHard: Boolean) {
        correctCount++
        currentQuestion = Question.generate(isHard)
        clearCanvas()
    }

    fun startStroke(x: Float, y: Float) {
        drawViewModel.startStroke(x, y)
    }

    fun addPoint(x: Float, y: Float) {
        drawViewModel.addPointToStroke(x, y)
    }

    fun endStroke() {
        drawViewModel.endStroke()
    }

    fun clearCanvas() {
        drawViewModel.clearCanvas()
    }
}