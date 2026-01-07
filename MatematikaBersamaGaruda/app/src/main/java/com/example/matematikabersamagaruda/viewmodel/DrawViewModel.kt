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
        recognizeDigit(newStrokes.last()) // ← Simulated for now
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
//    private fun recognizeDigit(stroke: List<Pair<Float, Float>>) {
////        // Convert stroke to 28x28 bitmap
////        val bitmap = convertStrokesToBitmap(listOf(stroke))
////
////        // Convert bitmap to input tensor
////        val input = bitmapToInputArray(bitmap)
////
////        // Prepare output array [1][10]
////        val output = Array(1) { FloatArray(10) }
////
////        // Run the model
////        tflite.run(input, output)
////
////        // Get the index of the max probability → predicted digit
////        val predictedDigit = output[0].indices.maxByOrNull { output[0][it] } ?: -1
////
////        // Append to recognizedDigits in UI state
////        uiState = uiState.copy(
////            recognizedDigits = uiState.recognizedDigits + predictedDigit.toString()
////        )
//        // 1) Preprocess: crop→scale→pad→center into a 28×28 bitmap
//        val bitmap = preprocess(listOf(stroke))
//
//        // 2) Convert bitmap to input tensor
//        val input = bitmapToInputArray(bitmap)
//
//        // 3) Run inference
//        val output = Array(1) { FloatArray(10) }
//        tflite.run(input, output)
//
//        // 4) Extract predicted digit
//        val predicted = output[0].indices.maxByOrNull { output[0][it] } ?: -1
//
//        // 5) Update UI state
//        uiState = uiState.copy(
//            recognizedDigits = uiState.recognizedDigits + predicted.toString()
//        )
//    }


    fun clearCanvas() {
        uiState = DrawUiState()
    }

    private fun preprocess(strokes: List<List<Pair<Float, Float>>>): Bitmap {
        // 1) Render strokes at 28×28
        val raw28 = convertStrokesToBitmap(strokes)

        // 2) Find bounding box of white pixels
        var minX = 28; var minY = 28
        var maxX = 0;  var maxY = 0
        for (y in 0 until 28) for (x in 0 until 28) {
            if (raw28.getPixel(x,y) == AndroidColor.WHITE) {
                minX = min(minX, x)
                minY = min(minY, y)
                maxX = max(maxX, x)
                maxY = max(maxY, y)
            }
        }
        // If nothing drawn, return as‑is
        if (maxX < minX || maxY < minY) return raw28

        // 3) Crop
        val w = maxX - minX + 1
        val h = maxY - minY + 1
        val cropped = Bitmap.createBitmap(raw28, minX, minY, w, h)

        // 4) Scale so longer side = 20px
        val scale = 20f / max(w, h)
        val newW = (w * scale).toInt()
        val newH = (h * scale).toInt()
        val resized = Bitmap.createScaledBitmap(cropped, newW, newH, true)

        // 5) Center in 28×28 black
        val output = Bitmap.createBitmap(28, 28, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(output).apply { drawColor(AndroidColor.BLACK) }
        val left = (28 - newW) / 2f
        val top  = (28 - newH) / 2f
        canvas.drawBitmap(resized, left, top, null)

        return output
    }

    private fun convertStrokesToBitmap(strokes: List<List<Pair<Float,Float>>>): Bitmap {
        val bigSize = 500
        val bigBmp = Bitmap.createBitmap(bigSize, bigSize, Bitmap.Config.ARGB_8888)
        val bigCanvas = AndroidCanvas(bigBmp)
        bigCanvas.drawColor(Color.BLACK)

        val paint = Paint().apply {
            color = Color.WHITE
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

        // downsample to 28×28
        return Bitmap.createScaledBitmap(bigBmp, 28, 28, true)
    }

//    private fun convertStrokesToBitmap(strokes: List<List<Pair<Float,Float>>>): Bitmap {
//        val bigSize = 500
//        val bigBmp = Bitmap.createBitmap(bigSize, bigSize, Bitmap.Config.ARGB_8888)
//        val bigCanvas = AndroidCanvas(bigBmp)
//        bigCanvas.drawColor(AndroidColor.BLACK)
//
//        val paint = Paint().apply {
//            color = AndroidColor.WHITE
//            style = Paint.Style.STROKE
//            strokeWidth = 40f                 // thick on 500px canvas
//            strokeCap = Paint.Cap.ROUND
//            isAntiAlias = true
//        }
//
//        for (stroke in strokes) {
//            if (stroke.size < 2) continue
//            val path = Path().apply {
//                moveTo(stroke[0].first, stroke[0].second)
//                for (i in 1 until stroke.size) {
//                    lineTo(stroke[i].first, stroke[i].second)
//                }
//            }
//            bigCanvas.drawPath(path, paint)
//        }
//
//        // downsample into exactly 28×28
//        return Bitmap.createScaledBitmap(bigBmp, 28, 28, /*filter=*/true)
//    }

//    fun convertStrokesToBitmap(strokes: List<List<Pair<Float, Float>>>): Bitmap {
//        val size = 28
//        val scale = size.toFloat() / 500f
//        val bitmap = createBitmap(size, size)
//        val canvas = AndroidCanvas(bitmap)
//
//        // 1) Fill background black
//        canvas.drawColor(AndroidColor.BLACK)
//
//        // 2) Paint in white
//        val paint = Paint().apply {
//            color = AndroidColor.WHITE
//            style = Paint.Style.STROKE
//            strokeWidth = 2f
//            isAntiAlias = true
//        }
//
//        for (stroke in strokes) {
//            if (stroke.size < 2) continue
//            val path = Path().apply {
//                moveTo(stroke[0].first * scale, stroke[0].second * scale)
//                for (i in 1 until stroke.size) {
//                    lineTo(stroke[i].first * scale, stroke[i].second * scale)
//                }
//            }
//            canvas.drawPath(path, paint)
//        }
//        return bitmap
//    }


//    private fun bitmapToInputArray(bitmap: Bitmap): ByteBuffer {
//        val input = ByteBuffer.allocateDirect(1 * 28 * 28 * 4).apply {
//            order(ByteOrder.nativeOrder())
//        }
//
//        for (y in 0 until 28) for (x in 0 until 28) {
//            val pixel = bitmap.getPixel(x, y)
//            val gray = (Color.red(pixel) +
//                    Color.green(pixel) +
//                    Color.blue(pixel)) / 3
//            // **no inversion**: white(255)->1.0, black(0)->0.0
//            val normalized = gray / 255f
//            input.putFloat(normalized)
//        }
//        return input
//    }

    private fun bitmapToInputArray(bitmap: Bitmap): ByteBuffer {
        val input = ByteBuffer.allocateDirect(1*28*28*4).apply {
            order(ByteOrder.nativeOrder())
        }
        for (y in 0 until 28) for (x in 0 until 28) {
            val pixel = bitmap.getPixel(x,y)
            val gray = (Color.red(pixel)+Color.green(pixel)+Color.blue(pixel))/3
            val normalized = gray/255f      // white(255)→1.0, black(0)→0.0
            input.putFloat(normalized)
        }
        return input
    }

}
