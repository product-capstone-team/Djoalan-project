package com.dicoding.djoalanapplication.ui.scanner

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.Executors

class DigitClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null

    var isInitialized = false
    private set

    private var executorService = Executors.newCachedThreadPool()
    private var inputImageWidth: Int = 0
    private var inputImageHeight: Int = 0
    private var modelInputSize: Int = 0

    companion object {
        private const val FILE_PATH = "digit_model.tflite"
        private const val FIRST_LABEL_SIZE = 13
        private const val SECOND_LABEL_SIZE = 10
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_BYTE_SIZE = 1
        private const val RESIZE_WIDTH = 231
        private const val RESIZE_HEIGHT = 87
        private const val TAG = "cek DigitClassifier"
    }

    fun initialize(): Task<Void?> {
        val task = TaskCompletionSource<Void?>()
        executorService.execute {
            try {
                initializeInterpreter()
                task.setResult(null)
            } catch (e: IOException) {
                task.setException(e)
            }
        }
        return task.task
    }

    private fun initializeInterpreter() {

        // TODO: LOAD TFLITE MODEL FROM ASSETS
        val assetManager = context.assets
        val model = loadModelFile(assetManager, FILE_PATH)
        val interpreter = Interpreter(model)


        // TODO: READ THE MODEL INPUT FROM INTERPRETER
        val inputShape = interpreter.getInputTensor(0).shape()
        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]
        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_BYTE_SIZE

        this.interpreter = interpreter
        isInitialized = true

        Log.d(TAG, "initialized model interpreter")
        Log.d(TAG, "shape1: ${inputShape[0]}")
        Log.d(TAG, "shape2: ${inputShape[1]}")
        Log.d(TAG, "shape3: ${inputShape[2]}")
        Log.d(TAG, "shape4: ${inputShape[3]}")
    }

    fun classifyAsync(bitmap: Bitmap): Task<String> {
        val task = TaskCompletionSource<String>()
        executorService.execute {
            val result = classify(bitmap)
            task.setResult(result)
        }
        return task.task
    }

    private fun classify(bitmap: Bitmap): String {
        check(isInitialized) {"TF Lite is not initialized yet"}

        // TODO: RESIZE IMAGE
        val resizeImage = Bitmap.createScaledBitmap(
            bitmap,
            RESIZE_WIDTH,
            RESIZE_HEIGHT,
            true
        )

        // TODO: CROP IMAGE
        val cropImage = Bitmap.createBitmap(
            resizeImage,
            0,
            20,
            inputImageWidth,
            inputImageHeight,
            null,
            false
        )

        // TODO: PREPARING OUTPUT CONTAINER AND PROCESS DATA
        val byteBuffer = convertBitmapToByteBuffer(cropImage)
        val output = arrayOf(Array(FIRST_LABEL_SIZE) { FloatArray(SECOND_LABEL_SIZE) })
        interpreter?.run(byteBuffer, output)

        // TODO: HANDLING OUTPUT
        val sb = StringBuilder()
        val result = output[0]
        var maxIndex: Int
        for ((i, data) in result.withIndex()) {
            maxIndex = data.indices.maxByOrNull { data[it] } ?: -1
            Log.d(TAG, "index $i value: $maxIndex")
            sb.append(maxIndex)
        }
        return sb.toString()
    }

    fun close() {
        executorService.execute {
            interpreter?.close()
            Log.d(TAG, "shutdown interpreter")
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            val normalizePixelValue = (r + g + b) / 3.0f / 255.0f
            byteBuffer.putFloat(normalizePixelValue)
        }

        return byteBuffer
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

}