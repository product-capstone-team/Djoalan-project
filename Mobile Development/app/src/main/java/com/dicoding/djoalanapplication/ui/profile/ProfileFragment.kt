package com.dicoding.djoalanapplication.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dicoding.djoalanapplication.LoginActivity
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.databinding.FragmentProfileBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.view.WindowManager
import com.dicoding.djoalanapplication.ui.TransactionViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var viewModel: AuthenticationViewModel
    @Inject lateinit var sessionViewModel: SessionViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    // binding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        transactionViewModel = ViewModelProvider(this@ProfileFragment, VMFactory(context))[TransactionViewModel::class.java]
        viewModel = ViewModelProvider(this@ProfileFragment, VMFactory(context))[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        sessionViewModel.moveIntoTopNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionViewModel.getUserAccType().observe(viewLifecycleOwner) { isBusinessAcc ->
            if (isBusinessAcc) {
                binding.QRCodeImage.visibility = View.VISIBLE
                binding.storeName.visibility = View.VISIBLE
                binding.storeNameText.visibility = View.VISIBLE
                binding.company.visibility = View.VISIBLE
                binding.companyText.visibility = View.VISIBLE
                binding.storeLocation.visibility = View.VISIBLE
                binding.storeLocationText.visibility = View.VISIBLE
            } else {
                binding.QRCodeImage.visibility = View.GONE
                binding.storeName.visibility = View.GONE
                binding.storeNameText.visibility = View.GONE
                binding.company.visibility = View.GONE
                binding.companyText.visibility = View.GONE
                binding.storeLocation.visibility = View.GONE
                binding.storeLocationText.visibility = View.GONE
            }
        }

        viewModel.getUserInfoFromDatabase().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.inputProfileName.text = user.name
                binding.inputProfileEmail.text = user.email
                binding.storeNameText.text = user.storeName
                binding.companyText.text = user.company
                getQRCode(user.userId)
            }
        }

        binding.buttonProfileChangeAccount.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_businessRegisterFragment)
        }

        binding.buttonProfileLogout.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setMessage("Are you sure want to Logout?")
                .setPositiveButton("yes") {_, _ ->
                    viewModel.logout()
                    viewModel.deleteUserInfo()
                    sessionViewModel.deleteSession()
                    transactionViewModel.deleteAllTransactionAndProducts()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("no") {_, _ -> }
                .create().show()

        }

//        getQRCode()
    }

    @Suppress("DEPRECATION")
    private fun getQRCode(userId: String) {
        val manager: WindowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3/4

        val QRGenerator = QRGEncoder(userId, null, QRGContents.Type.TEXT, smallerDimension)
        QRGenerator.colorBlack = Color.BLACK
        QRGenerator.colorWhite = Color.WHITE

        try {

            val bitmap = QRGenerator.bitmap
            binding.QRCodeImage.setImageBitmap(bitmap)

        } catch (e: Exception) {
            Log.d("cek qr code", e.message.toString())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("cek", "onDestroyViewFragment: success")
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("cek", "onDetachFragment: success")
    }

}