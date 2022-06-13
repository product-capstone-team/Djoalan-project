package com.dicoding.djoalanapplication.ui.business

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.djoalanapplication.LoginActivity
import com.dicoding.djoalanapplication.databinding.FragmentBusinessRegisterBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.network.response.StoreLocation
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.TransactionViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BusinessRegisterFragment : Fragment() {

    private var _binding: FragmentBusinessRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var session: SessionViewModel
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var userId: String

    companion object {
        private const val ANIMATION_TIME: Long = 350
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        transactionViewModel = ViewModelProvider(
            this@BusinessRegisterFragment,
            VMFactory(context)
        )[TransactionViewModel::class.java]
        viewModel = ViewModelProvider(this@BusinessRegisterFragment, VMFactory(context))[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBusinessRegisterBinding.inflate(inflater, container, false)

//        playAnimation()
        session.moveIntoBottomNav()



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()

        viewModel.getUserInfoFromDatabase().observe(viewLifecycleOwner) {
            if (it != null) {
                userId = it.userId
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.registerButtonBusiness.setOnClickListener {
            updateAccount()
        }



    }

    private fun updateAccount() {

        val ID = userId
        val storeName = binding.editRegisterStorenameBusiness.text.toString()
        val company = binding.editRegisterCompanyBusiness.text.toString()
        val location = StoreLocation(
            0.0,
            0.0
        )

        viewModel.update(ID, storeName, company, location).observe(viewLifecycleOwner) {
            if (it != null) {

                if (!it.error) {
                    Toast.makeText(requireContext(), "Success! please login again.", Toast.LENGTH_SHORT).show()
                    viewModel.logout()
                    viewModel.deleteUserInfo()
                    session.deleteSession()
                    transactionViewModel.deleteAllTransactionAndProducts()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "fail to update account. please try again later", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLoginBusiness.visibility = View.VISIBLE
        } else {
            binding.progressBarLoginBusiness.visibility = View.GONE
        }
    }

    private fun playAnimation() {

        val imglogo =
            ObjectAnimator.ofFloat(binding.imageViewRegisterBusiness, View.ALPHA, 1f).setDuration(
                ANIMATION_TIME)
        val nameText =
            ObjectAnimator.ofFloat(binding.labelRegisterStorenameBusiness, View.ALPHA, 1f).setDuration(
                ANIMATION_TIME)
        val nameInput = ObjectAnimator.ofFloat(binding.editRegisterStorenameBusiness, View.ALPHA, 1f)
            .setDuration(ANIMATION_TIME)
        val companyText =
            ObjectAnimator.ofFloat(binding.labelRegisterNamecompanyBusiness, View.ALPHA, 1f).setDuration(
                ANIMATION_TIME)
        val companyInput =
            ObjectAnimator.ofFloat(binding.editRegisterCompanyBusiness, View.ALPHA, 1f).setDuration(
                ANIMATION_TIME)
//        val locationText =
//            ObjectAnimator.ofFloat(binding.labelRegisterStorelocationBusiness, View.ALPHA, 1f).setDuration(
//                ANIMATION_TIME)
//        val locationInput =
//            ObjectAnimator.ofFloat(binding.editRegisterStoreLocationBusiness, View.ALPHA, 1f).setDuration(
//                ANIMATION_TIME)
        val buttonRegister =
            ObjectAnimator.ofFloat(binding.registerButtonBusiness, View.ALPHA, 1f).setDuration(
                ANIMATION_TIME)

        AnimatorSet().apply {
            playSequentially(imglogo,nameText,nameInput,companyText,companyInput, buttonRegister)
            start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}