package com.dicoding.djoalanapplication.ui.cart

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.adapter.ListItemAdapter
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.databinding.FragmentCartBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.network.response.SafeArgsItems
import com.dicoding.djoalanapplication.ui.OrderViewModel
import com.dicoding.djoalanapplication.ui.ProductViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.ui.scanner.ResultScanFragment
import com.dicoding.djoalanapplication.util.Result
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var session: SessionViewModel
    private lateinit var productViewModel: ProductViewModel
    private var qrcode: String = ""
    private var totalPrice: Int = 0
    private var findBarcode: String = ""

    private val sharedViewModel: OrderViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)

        productViewModel = ViewModelProvider(this@CartFragment, VMFactory(context))[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        session.moveIntoTopNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session.getStoreId().observe(viewLifecycleOwner) {
            qrcode = it
        }

        sharedViewModel.formatPrice.observe(viewLifecycleOwner) {
            binding.totalPriceCart.text = it
        }


        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_cart_to_orderSummaryFragment2)
        }

        binding.btnOpenScanner.setOnClickListener {
            findNavController()
                .navigate(R.id.action_navigation_cart_to_resultScanFragment)
        }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(ResultScanFragment.RESULT_BARCODE)
            ?.observe(viewLifecycleOwner) { result ->
                findBarcode = result
                getItemToCart(findBarcode)
//                Toast.makeText(requireContext(), "result: $result", Toast.LENGTH_SHORT).show()
//                productViewModel.getItem(storeId = qrcode, barcodeProduct = findBarcode).observe(viewLifecycleOwner) { data ->
//                    if (data != null) {
//                        when (data) {
//                            is Result.Loading -> {
//                                binding.progressBarCart.visibility = View.VISIBLE
//                            }
//                            is Result.Error -> {
//                                binding.progressBarCart.visibility = View.GONE
//                                Toast.makeText(requireContext(), "fail to get item", Toast.LENGTH_SHORT).show()
//                            }
//                            is Result.Success -> {
//                                binding.progressBarCart.visibility = View.GONE
//                                displayAdapter(data.data)
//                            }
//                        }
//                    }
//                }.also { findBarcode = "" }
            }


//        productViewModel.getItem(storeId = qrcode, barcodeProduct = findBarcode).observe(viewLifecycleOwner) { data ->
//            if (data != null) {
//                when (data) {
//                    is Result.Loading -> {
//                        binding.progressBarCart.visibility = View.VISIBLE
//                    }
//                    is Result.Error -> {
//                        binding.progressBarCart.visibility = View.GONE
//                        Toast.makeText(requireContext(), "fail to get item", Toast.LENGTH_SHORT).show()
//                    }
//                    is Result.Success -> {
//                        binding.progressBarCart.visibility = View.GONE
//                        displayAdapter(data.data)
//                    }
//                }
//            }
//        }
    }

    private fun getItemToCart(barcode: String) {

        productViewModel.getItem(storeId = qrcode, barcodeProduct = barcode).observe(viewLifecycleOwner) { data ->
            if (data != null) {
                when (data) {
                    is Result.Loading -> {
                        binding.progressBarCart.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBarCart.visibility = View.GONE
                        Toast.makeText(requireContext(), "fail to get item", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.progressBarCart.visibility = View.GONE
                        displayAdapter(data.data)
                    }
                }
            }
        }

    }

    private fun displayAdapter(items: List<Product>) {
        if (items.isNotEmpty()) {

//            items.forEach {
//                totalPrice += it.price
//            }
            val listBarcodeId = ArrayList<String>()
            items.forEach {
                totalPrice += it.price
                listBarcodeId.add(it.productId)
            }
            sharedViewModel.setListOfProducts(listBarcodeId)
            sharedViewModel.setPrice(totalPrice)

            val adapter = ListItemAdapter(items)
            val linearLayout = LinearLayoutManager(requireContext())
            binding.rvCart.adapter = adapter
            binding.rvCart.layoutManager = linearLayout
            binding.rvCart.addItemDecoration(DividerItemDecoration(requireContext(),linearLayout.orientation))

                adapter.setOnItemCallback( object : ListItemAdapter.OnItemClickListener {
                    override fun onItemClicked(data: Product) {
                        goToDetailProductPage(data)
                    }
                })


//            val placeHolder = StringBuilder().append("Rp ").append(totalPrice)
//            binding.totalPriceCart.text = placeHolder
//            sharedViewModel.price.observe(viewLifecycleOwner) {
//                binding.totalPriceCart.text = it
//            }

        }
    }
    private fun goToDetailProductPage(items: Product){
        val data = SafeArgsItems(
            items
        )
        val toDetailProduct = CartFragmentDirections.actionNavigationCartToDetailProductFragment(data)
        findNavController().navigate(toDetailProduct)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.remove(ResultScanFragment.RESULT_BARCODE)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        findBarcode = ""
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findBarcode = ""
        _binding = null
    }

}