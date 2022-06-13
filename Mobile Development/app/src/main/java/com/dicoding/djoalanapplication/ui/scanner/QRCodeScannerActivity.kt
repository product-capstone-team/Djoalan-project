package com.dicoding.djoalanapplication.ui.scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.djoalanapplication.databinding.ActivityQrcodeScannerBinding
import com.dicoding.djoalanapplication.databinding.BottomLayoutQrcodeScannerBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.util.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class QRCodeScannerActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityQrcodeScannerBinding
    private lateinit var userViewModel: AuthenticationViewModel
    @Inject lateinit var sessionViewModel: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Scan QR Code"
        userViewModel = ViewModelProvider(this@QRCodeScannerActivity, VMFactory(this))[AuthenticationViewModel::class.java]

        cameraExecutor = Executors.newSingleThreadExecutor()

        sessionViewModel.getStoreId().observe(this) {
            if (it != "") {
                Toast.makeText(this, "you can start scan barcode", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val listenableFuture = ProcessCameraProvider.getInstance(this)

        listenableFuture.addListener({
            val cameraProvider: ProcessCameraProvider = listenableFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { preview ->
                    preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            val analyzer = ImageAnalyzer()
            imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis,
                    preview,
                    imageCapture
                )

                analyzer.getResult().observe(this) {
                    for (result in it) {

                        displayResult(result)
                    }
                }


            } catch (e: Exception) {
                Toast.makeText(this, "Failed to show camera", Toast.LENGTH_SHORT).show()
                Log.d("cek", e.printStackTrace().toString())
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun displayResult(res: String) {
//        binding.apply {
//            labelResultQrcode.visibility = View.VISIBLE
//            textQrcode.visibility = View.VISIBLE
//            textQrcode.text = res ?: ""
//        }
        userViewModel.getStoreInfo(res).observe(this) { response ->

            if (response != null) {
                when (response) {
                    is Result.Loading -> {
                        binding.bottomLayout.apply {
                            progressBar.visibility = View.VISIBLE
                            msgNoitem.visibility = View.GONE
                        }
                    }
                    is Result.Error -> {
                        binding.bottomLayout.apply {
                            progressBar.visibility = View.GONE
                            msgNoitem.visibility = View.VISIBLE
                        }
                        Toast.makeText(this, "Error: ${response.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.bottomLayout.apply {
                            storeNameBottom.text = response.data.data?.user?.storeName
                            companyNameBottom.text = response.data.data?.user?.company

                            progressBar.visibility = View.GONE
                            msgNoitem.visibility = View.GONE
                            layoutOnBottom.visibility = View.VISIBLE
                            labelShopBottom.visibility = View.VISIBLE

                            layoutOnBottom.setOnClickListener {
                                // TODO: saving the store id for query purpose
                                sessionViewModel.saveStoreId(response.data.data?.user?.id.toString())
                                finish()
                            }
                        }

                    }
                }
            }

        }


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