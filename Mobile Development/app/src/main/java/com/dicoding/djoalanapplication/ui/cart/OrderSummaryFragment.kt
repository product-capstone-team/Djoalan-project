package com.dicoding.djoalanapplication.ui.cart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.databinding.FragmentOrderSummaryBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.OrderViewModel
import com.dicoding.djoalanapplication.ui.TransactionViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OrderSummaryFragment : Fragment() {

    private var _binding: FragmentOrderSummaryBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var session: SessionViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    private val sharedViewModel: OrderViewModel by activityViewModels()
    private var totalPrice: Int = 0
    private lateinit var listOfItems: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)

        session.moveIntoBottomNav()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        transactionViewModel = ViewModelProvider(this@OrderSummaryFragment, VMFactory(context))[TransactionViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.listProduct.observe(viewLifecycleOwner) {
            listOfItems = it
        }

        sharedViewModel.price.observe(viewLifecycleOwner) {
            totalPrice = it
        }

        sharedViewModel.formatPrice.observe(viewLifecycleOwner) {
            binding.totalPurchaseOrderSummary.text = it
        }

        sharedViewModel.paymentMethod.observe(viewLifecycleOwner) {
            Log.d("cek paymentMethod", it.toString())
            payment(it)
        }

        binding.btnOrderSummary.setOnClickListener {

            var paymentMethod: String?
            val checkedRadioButton = binding.rgOrderSummary.checkedRadioButtonId
            if (checkedRadioButton != -1) {
                paymentMethod =
                    when (checkedRadioButton) {
                        R.id.rb_gopay -> {
                            binding.rbGopay.text.toString()
                        }
                        R.id.rb_dana -> {
                            binding.rbDana.text.toString()
                        }
                        R.id.rb_shopeepay -> {
                            binding.rbShopeepay.text.toString()
                        }
                        else -> null
                    }
                sharedViewModel.setPaymentMethod(paymentMethod)
//                payment(paymentMethod)
            }

        }

        transactionViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun payment(paymentMethod: String?) {

        if (paymentMethod != null && totalPrice != 0) {

            try {

                transactionViewModel
                    .confirmPayment(total = totalPrice, paymentMethod.toString())
                    .observe(viewLifecycleOwner) { res ->
                    if (res != null ) {
                        if (!res.error!!) {

                            val webpage: Uri = Uri.parse(res.resp?.actions?.mobileWebCheckoutUrl)
                            val intentToBrowser = Intent(Intent.ACTION_VIEW, webpage)
                            openPaymentWebsite.launch(intentToBrowser)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("cek", "fail confirm payment")
            }

        } else {
            Toast.makeText(requireContext(), "Choose payment method first!", Toast.LENGTH_SHORT).show()
        }
    }

    private val openPaymentWebsite = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        Toast.makeText(requireContext(), "purchase complete", Toast.LENGTH_SHORT).show()

        // TODO: LIST ALL THE ITEMS AND REQUEST FOR NEW TRANSACTION
//        val listOfItems = ArrayList<String>()
//        listOfItems.add("T01")
//        listOfItems.add("T02")
        transactionViewModel.addTransaction(listOfItems).observe(viewLifecycleOwner) { res ->

            if (res != null ) {
                if (!res.error!!) {
                    findNavController().navigate(R.id.action_orderSummaryFragment2_to_orderStatusFragment2)
                } else {
                    Log.d("cek", "cause: ${res.message ?: "Need to pay first"}")
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSummary.visibility = View.VISIBLE
        } else {
            binding.progressBarSummary.visibility = View.GONE
        }
    }

}