package com.dicoding.djoalanapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.databinding.RowItemHomeBinding
import com.dicoding.djoalanapplication.network.response.SortedPriceItem

class BestPriceAdapter(private val listBestPrice: List<SortedPriceItem>) : RecyclerView.Adapter<BestPriceAdapter.ViewHolder>()  {

    class ViewHolder (var binding: RowItemHomeBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val price = listBestPrice[position].price.toString()
        val image = listBestPrice[position].imageUrl
        val company = listBestPrice[position].companyId.company
        holder.binding.priceItemHome.text = price
        holder.binding.tvCompanyHome.text = company
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.imageView3)

    }

    override fun getItemCount(): Int = listBestPrice.size
}