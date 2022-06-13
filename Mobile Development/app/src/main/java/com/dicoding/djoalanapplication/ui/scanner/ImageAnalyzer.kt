package com.dicoding.djoalanapplication.ui.scanner

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat

class ImageAnalyzer: ImageAnalysis.Analyzer {

    private val scannerResult = MutableLiveData<List<String>>()
    fun getResult(): LiveData<List<String>> = scannerResult

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(proxy: ImageProxy) {
        proxy.image?.let { image ->

            val inputImage = InputImage.fromMediaImage(image, proxy.imageInfo.rotationDegrees)
            val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
            barcodeScanner.process(inputImage)
                .addOnSuccessListener {
                    readBarcode(it as List<Barcode>)
                    proxy.close()
                }
                .addOnFailureListener {
                    Log.d("cek", "failure to scan")
                    proxy.close()
                }
        }
    }

    private fun readBarcode(list: List<Barcode>) {
        val arrayList = ArrayList<String>()
        for (barcode in list) {
            Log.d("cek qr code", "isUserId: ${barcode.valueType == Barcode.TYPE_TEXT}")
            if (barcode.valueType == Barcode.TYPE_TEXT)
                arrayList.add(barcode.displayValue.toString())
        }
        scannerResult.value = arrayList
    }
}