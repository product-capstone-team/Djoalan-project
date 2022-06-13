package com.dicoding.djoalanapplication.ui.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.databinding.FragmentResultScanBinding
import com.dicoding.djoalanapplication.util.MediaHelper
import com.google.mlkit.vision.barcode.BarcodeScanner
import java.io.File


class ResultScanFragment : Fragment() {

    private var _binding: FragmentResultScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var digitModelClassifier: DigitClassifier
    private var resultBarcode: String = ""

    companion object {
        private const val TAG = "cek ResultScanFragment"
        const val RESULT_CODE_CAMERAX = 200
        const val PICTURE_KEY = "picture"
        const val CAMERA_SELECTOR_KEY = "isBackCamera"
        const val RESULT_BARCODE = "result_barcode"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        digitModelClassifier = DigitClassifier(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultScanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        digitModelClassifier.initialize().addOnFailureListener { e -> Log.d(TAG, "Error to setting up digit classifier.", e) }

        binding.scanBarcode.setOnClickListener {
            askPermission.launch(Manifest.permission.CAMERA)
        }

        binding.addBarcode.setOnClickListener {
            resultBarcode = binding.inputBarcode.text.toString()
            addItemToCart(resultBarcode)
        }
    }

    private val askPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intentToScanner = Intent(requireActivity(), BarcodeScannerActivity::class.java)
            launcherCameraX.launch(intentToScanner)
        } else {
            Toast.makeText(
                requireContext(),
                "you need permission to open camera",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_CODE_CAMERAX) {
            val myFile = it.data?.getSerializableExtra(PICTURE_KEY) as File
            val isBackCamera = it.data?.getBooleanExtra(CAMERA_SELECTOR_KEY, true) as Boolean

            val resultImage = MediaHelper.rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            if (digitModelClassifier.isInitialized) {
                digitModelClassifier
                    .classifyAsync(resultImage)
                    .addOnSuccessListener { result ->
                        Log.d(TAG, "success scanning: $result")
                        binding.inputBarcode.setText(result)


                    }
                    .addOnFailureListener { Log.d(TAG, "fail scanning") }
            }
            binding.previewImage.setImageBitmap(resultImage)
        }
    }

    private fun addItemToCart(barcode: String) {
        if (barcode != "") {
            findNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set(RESULT_BARCODE, barcode)
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        digitModelClassifier.close()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}