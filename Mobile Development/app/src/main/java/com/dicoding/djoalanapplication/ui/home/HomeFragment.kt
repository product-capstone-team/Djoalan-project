package com.dicoding.djoalanapplication.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.djoalanapplication.R
import com.dicoding.djoalanapplication.adapter.BestPriceAdapter
import com.dicoding.djoalanapplication.databinding.FragmentHomeBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.ProductViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import com.dicoding.djoalanapplication.ui.scanner.QRCodeScannerActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var session: SessionViewModel
    private lateinit var productViewModel: ProductViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productViewModel = ViewModelProvider(this@HomeFragment, VMFactory(context))[ProductViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        session.moveIntoTopNav()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconInfoHome.setOnClickListener {
            InformationDialogFragment().show(childFragmentManager, InformationDialogFragment.TAG)
        }

        binding.btnChangeAccountHome.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_businessRegisterFragment)
        }

        binding.scanButtonHome.setOnClickListener {
            askPermission.launch(Manifest.permission.CAMERA)
        }

        productViewModel.getBestPrice().observe(viewLifecycleOwner) {

            val adapter = BestPriceAdapter(it.sortedPrice)
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))

        }
    }

    private val askPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted ->
        if (isGranted) {
            val moveToScanner = Intent(requireActivity(), QRCodeScannerActivity::class.java)
            requireActivity().startActivity(moveToScanner)
        } else {
            Toast.makeText(
                requireContext(),
                "you need permission to open camera",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("cek", "onPause Home Page")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}