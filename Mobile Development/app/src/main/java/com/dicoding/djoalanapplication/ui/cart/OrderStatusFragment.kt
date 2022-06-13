package com.dicoding.djoalanapplication.ui.cart

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.databinding.FragmentOrderStatusBinding
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.OrderViewModel
import com.dicoding.djoalanapplication.ui.ProductViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import java.text.SimpleDateFormat
import java.util.*

class OrderStatusFragment : Fragment() {

    private var _binding: FragmentOrderStatusBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: AuthenticationViewModel
    private lateinit var productViewModel: ProductViewModel
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userViewModel = ViewModelProvider(this@OrderStatusFragment, VMFactory(context))[AuthenticationViewModel::class.java]
        productViewModel = ViewModelProvider(this@OrderStatusFragment, VMFactory(context))[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderStatusBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val date = formatter.format(currentTime)

        binding.tvDateStatus.text = date.toString()

        userViewModel.getUserInfoFromDatabase().observe(viewLifecycleOwner) {
            binding.userPurchase.text = it.name
        }

        binding.buttonToHome.setOnClickListener {
            orderViewModel.resetOrder()
            productViewModel.deleteAllProductsInDatabase()
            findNavController().navigate(R.id.action_orderStatusFragment_to_navigation_home)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}