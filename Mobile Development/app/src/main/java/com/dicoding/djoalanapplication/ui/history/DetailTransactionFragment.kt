package com.dicoding.djoalanapplication.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.djoalanapplication.adapter.DetailHistoryAdapter
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.databinding.FragmentDetailTransactionBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailTransactionFragment : Fragment() {

    private var _binding: FragmentDetailTransactionBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var session: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        session.moveIntoBottomNav()

        (activity as AppCompatActivity).supportActionBar?.title = "Detail Transaction"
        _binding = FragmentDetailTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = DetailTransactionFragmentArgs.fromBundle(arguments as Bundle).argsData.id
        val date = DetailTransactionFragmentArgs.fromBundle(arguments as Bundle).argsData.date
        val total = DetailTransactionFragmentArgs.fromBundle(arguments as Bundle).argsData.total
        val product = DetailTransactionFragmentArgs.fromBundle(arguments as Bundle).argsData.listProduct

        binding.orderIdText.text = id
        binding.dateTextDetail.text = date
        binding.totalPriceDetail.text = total.toString()

        getDetailAdapter(product)
        super.onViewCreated(view, savedInstanceState)

    }
    private fun getDetailAdapter(items: List<PurchasedItems>){
        val adapter = DetailHistoryAdapter(items)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView4.layoutManager = layoutManager
        binding.recyclerView4.addItemDecoration(DividerItemDecoration(requireContext(),layoutManager.orientation))
        binding.recyclerView4.adapter = adapter
    }

    companion object {
        private const val TAG = "cek DetailTransaction"
    }


}