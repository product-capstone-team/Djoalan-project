package com.dicoding.djoalanapplication.ui.business

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.databinding.FragmentBusinessAddItemBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.ProductViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.ui.scanner.ResultScanFragment
import com.dicoding.djoalanapplication.util.MediaHelper
import com.dicoding.djoalanapplication.util.Result
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class BusinessAddItemFragment : Fragment() {

    @Inject lateinit var session: SessionViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: AuthenticationViewModel

    private var _binding: FragmentBusinessAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var brand: String
    private lateinit var currentPicPath: String
    private var myFile: File? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productViewModel = ViewModelProvider(this@BusinessAddItemFragment, VMFactory(context))[ProductViewModel::class.java]
        userViewModel = ViewModelProvider(this@BusinessAddItemFragment, VMFactory(context))[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessAddItemBinding.inflate(inflater, container, false)

        session.moveIntoBottomNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        productViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.barcodeScannerImg.setOnClickListener {
            findNavController().navigate(R.id.action_businessAddItemFragment_to_resultScanFragment)
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<String>(ResultScanFragment.RESULT_BARCODE)
                ?.observe(viewLifecycleOwner) { result ->
                    Toast.makeText(requireContext(), "result, $result", Toast.LENGTH_SHORT).show()
                }
        }

        userViewModel.getUserInfoFromDatabase().observe(viewLifecycleOwner) {
            if (it != null) {
                brand = it.company ?: "unknown"
            }
        }

        binding.additemBtnBusiness.setOnClickListener {
            addItem()
        }

        binding.cameraBtnAdditemBusiness.setOnClickListener { askPermission.launch(Manifest.permission.CAMERA) }
        binding.galleryBtnAdditemBusin.setOnClickListener { accessGallery() }
    }

    private fun showLoading(loadingState: Boolean) {
        if (loadingState) {
            binding.progressBarAddItem.visibility = View.VISIBLE
        } else {
            binding.progressBarAddItem.visibility = View.GONE
        }
    }

    private fun addItem() {

        try {

            if (myFile != null) {

                val productId = binding.editAdditemBarcodeBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val name = binding.editAdditemProductnameBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val brand = binding.editAdditemBrandBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val expiredDate = binding.editAdditemExpireddateBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val price = binding.editAdditemPriceitemBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val category = binding.editAdditemCategoryBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())
                val quantity = binding.editAdditemTotalstockBusiness.text.trim().toString().toRequestBody("text/plain".toMediaType())

                val file = MediaHelper.reduceFileImage(myFile as File)
                val imageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "imageUrl",
                    filename = file.name,
                    imageFile
                )

                productViewModel.addItem(productId, name, brand, expiredDate, price, category, quantity, imageMultiPart).observe(viewLifecycleOwner) { res ->

                    if (res != null && !res.error) {
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Request Error!", Toast.LENGTH_SHORT).show()
                    }

                }.also {
                    binding.apply {
                        editAdditemBarcodeBusiness.setText("")
                        editAdditemCategoryBusiness.setText("")
                        editAdditemExpireddateBusiness.setText("")
                        editAdditemPriceitemBusiness.setText("")
                        editAdditemProductnameBusiness.setText("")
                        editAdditemTotalstockBusiness.setText("")
                        editAdditemBrandBusiness.setText("")
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Invalid Request", Toast.LENGTH_SHORT).show()
            Log.d("cek addItem", "cause: ${e.message.toString()}")
            Log.d("cek addItem", "cause: ${e.cause.toString()}")
        }

    }

    private fun openCamera() {
        val intentToCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intentToCamera.resolveActivity(requireActivity().packageManager)

        MediaHelper.createTempFile(requireActivity().application).also {
            val picUri: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.dicoding.djoalanapplication",
                it
            )
            currentPicPath = it.absolutePath
            intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, picUri)
            launcherIntentCamera.launch(intentToCamera)
        }
    }

    private fun accessGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val file = File(currentPicPath)
            myFile = file
            val result = BitmapFactory.decodeFile(file.path)
            binding.productPhotosAdditemBusiness.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val file = MediaHelper.uriToFile(selectedImage, requireContext())
            myFile = file
            binding.productPhotosAdditemBusiness.setImageURI(selectedImage)
        }
    }

    private val askPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(
                requireContext(),
                "you need permission to open camera",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("cek", "onStop add item fragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RESULT_BARCODE_BUSINESS = "result_business"
    }

}