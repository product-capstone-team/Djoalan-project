package com.dicoding.djoalanapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.djoalanapplication.data.transaction.TransactionAndPurchasedItems
import com.dicoding.djoalanapplication.databinding.RowHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class ListHistoryAdapter : ListAdapter<TransactionAndPurchasedItems, ListHistoryAdapter.ItemViewHolder>(ItemsComparator()) {

    private lateinit var onItemClickCallback: OnItemClickListener

    fun setOnItemCallback(onItemClickCallback: OnItemClickListener) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(position)) }
    }

    class ItemViewHolder(private val binding: RowHistoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(data: TransactionAndPurchasedItems) {
                    binding.orderOutputHistory.text = data.transaction.orderId
                    binding.totalOutputHistory.text = data.transaction.totalPrice.toString()
                    binding.dateOutputHistory.text = data.transaction.date
                }
            }

    class ItemsComparator: DiffUtil.ItemCallback<TransactionAndPurchasedItems>() {
        override fun areItemsTheSame(
            oldItem: TransactionAndPurchasedItems,
            newItem: TransactionAndPurchasedItems
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TransactionAndPurchasedItems,
            newItem: TransactionAndPurchasedItems
        ): Boolean {
            return oldItem.transaction.orderId == newItem.transaction.orderId
        }

    }

    interface OnItemClickListener {
        fun onItemClicked(data: TransactionAndPurchasedItems)
    }

}