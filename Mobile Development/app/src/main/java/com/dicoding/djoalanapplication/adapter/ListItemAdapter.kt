package com.dicoding.djoalanapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.transaction.TransactionAndProduct
import com.dicoding.djoalanapplication.databinding.RowItemBinding

class ListItemAdapter (private val listItem: List<Product>) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickListener

    fun setOnItemCallback(onItemClickCallback: OnItemClickListener) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder (var binding: RowItemBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productName = listItem[position].productName
        val price = listItem[position].price.toString()
        val image = listItem[position].imageUrl
        val category = listItem[position].category
        val qty = listItem[position].quantity.toString()
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.imageView)
        holder.binding.nameItemCardview.text = productName
        holder.binding.totalPriceCardview.text = price
        holder.binding.descItemCardview.text = category
        holder.binding.totalItemCardview.text = qty
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listItem[position]) }

    }

    override fun getItemCount(): Int = listItem.size

    interface OnItemClickListener {
        fun onItemClicked(data: Product)
    }

}