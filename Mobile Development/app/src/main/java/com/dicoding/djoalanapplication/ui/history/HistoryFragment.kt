package com.dicoding.djoalanapplication.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.djoalanapplication.adapter.DetailHistoryAdapter
import com.dicoding.djoalanapplication.adapter.ListHistoryAdapter
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.transaction.TransactionAndProduct
import com.dicoding.djoalanapplication.data.transaction.TransactionAndPurchasedItems
import com.dicoding.djoalanapplication.databinding.FragmentHistoryBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.network.response.SafeArgsData
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.TransactionViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.util.Result
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var session: SessionViewModel
    private lateinit var orderViewModel: TransactionViewModel
    private lateinit var userViewModel: AuthenticationViewModel
    private var userId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        orderViewModel = ViewModelProvider(
            this@HistoryFragment,
            VMFactory(context)
        )[TransactionViewModel::class.java]

        userViewModel = ViewModelProvider(
            this@HistoryFragment,
            VMFactory(context)
        )[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        session.moveIntoTopNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getUserInfoFromDatabase().observe(viewLifecycleOwner) { data ->
            if (data != null) {
                userId = data.userId
            }
        }

//        orderViewModel.getListOfTransaction(userId = "62a157b5494f4885d025e0d3").observe(viewLifecycleOwner) { res ->
//            if (res != null) {
//                when (res) {
//                    is Result.Loading -> {
//                        binding.progressBarHistory.visibility = View.VISIBLE
//                    }
//                    is Result.Success -> {
//
//                        binding.progressBarHistory.visibility = View.GONE
//                        res.data[0].products
//                        res.data[0].transaction
//                        Toast.makeText(requireContext(), "Success lolll", Toast.LENGTH_SHORT).show()
////                        Log.d("cek history fragment", "success ${res.data[0].transaction.totalPrice}")
//                        displayOnAdapter(res.data)
//                    }
//                    is Result.Error -> {
//                        binding.progressBarHistory.visibility = View.GONE
//                        Toast.makeText(requireContext(), "Failure ${res.error}", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }


    }

    override fun onResume() {
        super.onResume()

        orderViewModel.getListOfTransaction(userId = userId).observe(viewLifecycleOwner) { res ->
            if (res != null) {
                when (res) {
                    is Result.Loading -> {
                        binding.progressBarHistory.visibility = View.VISIBLE
                    }
                    is Result.Success -> {

                        binding.progressBarHistory.visibility = View.GONE
                        displayOnAdapter(res.data)
                    }
                    is Result.Error -> {
                        binding.progressBarHistory.visibility = View.GONE
                        Toast.makeText(requireContext(), "Failure ${res.error}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun displayOnAdapter(items: List<TransactionAndPurchasedItems>) {
        val adapter = ListHistoryAdapter()
        binding.rvHistory.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))

        for (order in items) {
            adapter.submitList(items)
            adapter.setOnItemCallback( object : ListHistoryAdapter.OnItemClickListener {
                override fun onItemClicked(data: TransactionAndPurchasedItems) {
                    goToDetailTransactionPage(data)
                }
            })
        }

    }

    private fun goToDetailTransactionPage(items: TransactionAndPurchasedItems) {

//        toDetailTransaction.id = items.transaction.orderId
//        toDetailTransaction.date = items.transaction.date
//        toDetailTransaction.total = items.transaction.totalPrice
        val data = SafeArgsData(
            items.transaction.orderId,
            items.transaction.date,
            items.transaction.totalPrice,
            items.purchasedItems!!
        )
        val toDetailTransaction = HistoryFragmentDirections.actionNavigationHistoryToDetailTransactionFragment(data)
        findNavController().navigate(toDetailTransaction)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HistoryFragment"
    }
}
