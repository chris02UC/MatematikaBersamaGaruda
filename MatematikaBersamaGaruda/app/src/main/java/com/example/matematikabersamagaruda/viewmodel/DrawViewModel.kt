package com.example.matematikabersamagaruda.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.runtime.*
import com.example.matematikabersamagaruda.model.DrawUiState
import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min
import kotlin.math.max

class DrawViewModel(application: Application) : AndroidViewModel(application) {


    private val tflite: Interpreter by lazy {
        val afd = getApplication<Application>().assets.openFd("handwritelogic.tflite")
        val channel = afd.createInputStream().channel
        val buf = channel.map(
            java.nio.channels.FileChannel.MapMode.READ_ONLY,
            afd.startOffset,
            afd.declaredLength
        )

        // LiteRT’s Interpreter supports the newer ops out of the box
        Interpreter(buf)
    }

    var uiState by mutableStateOf(DrawUiState())
        private set

    fun startStroke(x: Float, y: Float) {
        uiState = uiState.copy(currentStroke = listOf(x to y))
    }

    fun addPointToStroke(x: Float, y: Float) {
        uiState = uiState.copy(
            currentStroke = uiState.currentStroke + (x to y)
        )
    }

    fun endStroke() {
        val newStrokes = uiState.allStrokes + listOf(uiState.currentStroke)
        uiState = uiState.copy(
            currentStroke = emptyList(),
            allStrokes = newStrokes
        )
        recognizeDigit(newStrokes.last())
    }

    private fun recognizeDigit(stroke: List<Pair<Float,Float>>) {
        viewModelScope.launch(Dispatchers.Default) {
            // 1) Preprocess into 28×28 exactly
            val bmp = preprocess(listOf(stroke))
            // 2) Build input buffer
            val input = bitmapToInputArray(bmp)
            // 3) Run TF Lite
            val output = Array(1){ FloatArray(10) }
            tflite.run(input, output)
            // 4) Pick best
            val pred = output[0].indices.maxByOrNull { output[0][it] } ?: -1
            // 5) Post back on UI
            withContext(Dispatchers.Main) {
                uiState = uiState.copy(recognizedDigits = uiState.recognizedDigits + pred)
            }
        }
    }

    fun clearCanvas() {
        uiState = DrawUiState()
    }

    private fun preprocess(strokes: List<List<Pair<Float, Float>>>): Bitmap {
        // 1) Render strokes at High Resolution (500x500) to preserve detail
        val rawHighRes = convertStrokesToBitmap(strokes)

        // 2) Find bounding box of white pixels on the HIGH RES bitmap
        var minX = rawHighRes.width
        var minY = rawHighRes.height
        var maxX = 0
        var maxY = 0

        for (y in 0 until rawHighRes.height) {
            for (x in 0 until rawHighRes.width) {
                // Check if pixel is not black (contains some drawing)
                // Using simple threshold or check for WHITE
                if (rawHighRes.getPixel(x, y) != AndroidColor.BLACK) {
                    minX = min(minX, x)
                    minY = min(minY, y)
                    maxX = max(maxX, x)
                    maxY = max(maxY, y)
                }
            }
        }

        // If nothing drawn, return a blank 28x28 bitmap
        if (maxX < minX || maxY < minY) {
            return Bitmap.createScaledBitmap(rawHighRes, 28, 28, true)
        }

        // 3) Crop to bounding box (High Resolution)
        val w = maxX - minX + 1
        val h = maxY - minY + 1
        val cropped = Bitmap.createBitmap(rawHighRes, minX, minY, w, h)

        // 4) Scale so longer side = 20px
        val targetSize = 20f
        val scale = targetSize / max(w, h)
        val newW = (w * scale).toInt()
        val newH = (h * scale).toInt()

        // Use filter=true for smooth downscaling
        val resized = Bitmap.createScaledBitmap(cropped, newW, newH, true)

        // 5) Center by Center of Mass in 28x28 black canvas
        val output = Bitmap.createBitmap(28, 28, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(output).apply { drawColor(AndroidColor.BLACK) }

        // Calculate Center of Mass
        var sumX = 0f
        var sumY = 0f
        var totalMass = 0f

        for (y in 0 until newH) {
            for (x in 0 until newW) {
                val pixel = resized.getPixel(x, y)
                // Calculate brightness (0.0 to 255.0)
                val mass = (AndroidColor.red(pixel) + AndroidColor.green(pixel) + AndroidColor.blue(pixel)) / 3f
                sumX += x * mass
                sumY += y * mass
                totalMass += mass
            }
        }

        var drawLeft = (28 - newW) / 2f
        var drawTop = (28 - newH) / 2f

        if (totalMass > 0) {
            val comX = sumX / totalMass
            val comY = sumY / totalMass

            drawLeft = 14f - comX
            drawTop = 14f - comY
        }

        canvas.drawBitmap(resized, drawLeft, drawTop, null)

        return output
    }

    private fun convertStrokesToBitmap(strokes: List<List<Pair<Float,Float>>>): Bitmap {
        val bigSize = 500
        val bigBmp = Bitmap.createBitmap(bigSize, bigSize, Bitmap.Config.ARGB_8888)
        val bigCanvas = AndroidCanvas(bigBmp)
        bigCanvas.drawColor(AndroidColor.BLACK)

        val paint = Paint().apply {
            color = AndroidColor.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 40f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }

        strokes.forEach { stroke ->
            if (stroke.size < 2) return@forEach
            val path = Path().apply {
                moveTo(stroke[0].first, stroke[0].second)
                stroke.drop(1).forEach { (x, y) -> lineTo(x, y) }
            }
            bigCanvas.drawPath(path, paint)
        }

        return bigBmp
    }

    private fun bitmapToInputArray(bitmap: Bitmap): ByteBuffer {
        val input = ByteBuffer.allocateDirect(1*28*28*4).apply {
            order(ByteOrder.nativeOrder())
        }
        for (y in 0 until 28) for (x in 0 until 28) {
            val pixel = bitmap.getPixel(x,y)
            val gray = (Color.red(pixel)+Color.green(pixel)+Color.blue(pixel))/3
            val normalized = gray/255f
            input.putFloat(normalized)
        }
        return input
    }
}