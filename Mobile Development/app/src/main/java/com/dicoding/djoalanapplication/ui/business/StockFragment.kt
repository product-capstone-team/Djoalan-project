package com.dicoding.djoalanapplication.ui.business

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.adapter.ListItemAdapter
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.databinding.FragmentStockBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.network.response.SafeArgsItems
import com.dicoding.djoalanapplication.ui.ProductViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.ui.cart.CartFragmentDirections
import com.dicoding.djoalanapplication.util.Result
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StockFragment : Fragment() {

    @Inject lateinit var session: SessionViewModel
    private lateinit var productViewModel: ProductViewModel

    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productViewModel = ViewModelProvider(this@StockFragment, VMFactory(context))[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStockBinding.inflate(inflater, container, false)

        session.moveIntoTopNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStockAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_stock_to_businessAddItemFragment)
        }

        productViewModel.getAllStock().observe(viewLifecycleOwner) { data ->
            if (data != null) {
                when (data) {
                    is Result.Loading -> {
                        binding.progressBarStock.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBarStock.visibility = View.GONE
                        Toast.makeText(requireContext(), data.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        binding.progressBarStock.visibility = View.GONE
                        showProducts(data.data)
                    }
                }
            }
        }
    }

    private fun showProducts(products: List<Product>) {
        if (products.isNotEmpty()) {
            val adapter = ListItemAdapter(products)
            val layoutManager = LinearLayoutManager(requireContext())
            binding.rvStock.adapter = adapter
            binding.rvStock.layoutManager = layoutManager
            binding.rvStock.addItemDecoration(DividerItemDecoration(requireContext(),layoutManager.orientation))

                adapter.setOnItemCallback( object : ListItemAdapter.OnItemClickListener {
                    override fun onItemClicked(data: Product) {
                        goToDetailProductStockPage(data)
                }
            })
        }
    }
    private fun goToDetailProductStockPage(items: Product){
        val data = SafeArgsItems(
            items
        )
        val toDetailProduct = StockFragmentDirections.actionNavigationStockToDetailProductFragment(data)
        findNavController().navigate(toDetailProduct)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}