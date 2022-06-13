package com.dicoding.djoalanapplication.ui.detailproduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.databinding.FragmentDetailProductBinding
import com.dicoding.djoalanapplication.databinding.FragmentDetailTransactionBinding
import com.dicoding.djoalanapplication.ui.history.DetailTransactionFragmentArgs


class DetailProductFragment : Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Detail Product"
        _binding = FragmentDetailProductBinding.inflate(inflater, container,false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productDetail= DetailProductFragmentArgs.fromBundle(arguments as Bundle).detailItemArgs.listProduct
        binding.nameDetailProduct.text = productDetail.productName
        binding.quantityEdit.text = productDetail.quantity.toString()
        binding.brandNameEdit.text = productDetail.brand
        binding.expiredDateEdit.text = productDetail.expireDate
        binding.categoryNameEdit.text = productDetail.category
        binding.priceDetailProduct.text = productDetail.price.toString()
        Glide.with(this)
            .load(productDetail.imageUrl)
            .into(binding.imgDetailProduct)
    }

}