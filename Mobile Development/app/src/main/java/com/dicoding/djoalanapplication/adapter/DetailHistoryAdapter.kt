package com.dicoding.djoalanapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.databinding.RowDetailHistoryItemBinding


class DetailHistoryAdapter  (private val listItem: List<PurchasedItems>) : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {

    class ViewHolder (var binding: RowDetailHistoryItemBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val binding = RowDetailHistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val nameItem= listItem[position].productName
        val brand = listItem[position].brand
        val expiredDate = listItem[position].expireDate
        val category = listItem[position].category
        val qty = listItem[position].quantity.toString()
        val price = listItem[position].price.toString()
        val image = listItem[position].imageUrl

        holder.binding.nameItemCvHistoryDetail.text = nameItem
        holder.binding.descItemCardview.text = brand
        holder.binding.expiredDateTextHistoryDetail.text = expiredDate
        holder.binding.categoryTextHistoryDetail.text = category
        holder.binding.qtyHistoryDetailText.text = qty
        holder.binding.totalPriceHistoryDetail.text = price
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.imgViewDetailHistory)



    }

    override fun getItemCount(): Int = listItem.size


}