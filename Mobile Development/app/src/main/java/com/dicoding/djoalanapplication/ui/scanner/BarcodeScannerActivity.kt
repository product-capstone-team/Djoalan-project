package com.dicoding.djoalanapplication.ui.scanner

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.djoalanapplication.databinding.ActivityBarcodeScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.dicoding.djoalanapplication.util.MediaHelper.createFile
import java.lang.Exception

class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarcodeScannerBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    companion object {
        private const val RESOLUTION_WIDTH = 231
        private const val RESOLUTION_HEIGHT = 87
        private const val TAG = "cek BarcodeScanner"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.root.setBackgroundColor(Color.BLACK)

        binding.outlineTakePicture.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {

        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra(ResultScanFragment.PICTURE_KEY, photoFile)
                    intent.putExtra(ResultScanFragment.CAMERA_SELECTOR_KEY, cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(ResultScanFragment.RESULT_CODE_CAMERAX, intent)
                    finish()

                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@BarcodeScannerActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )

    }

    private fun startCamera() {

        val listenableFuture = ProcessCameraProvider.getInstance(this)
        listenableFuture.addListener({
            val cameraProvider: ProcessCameraProvider = listenableFuture.get()
            val preview = Preview.Builder()
                .setTargetResolution(Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                .build()
                .also { prev ->
                    prev.setSurfaceProvider(binding.viewFinderBarcode.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.d(TAG, "cause: ${e.message.toString()}")
                Toast.makeText(this@BarcodeScannerActivity, "Gagal memunculkan kamera lol", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))

    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}