package com.daily_school.daily_school.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.SchoolNameRvItemBinding

class SchoolInfoAdapter : ListAdapter<Row, SchoolInfoAdapter.SchoolInfoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolInfoViewHolder {
        val binding = SchoolNameRvItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return SchoolInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class SchoolInfoViewHolder(private val binding : SchoolNameRvItemBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(item : Row){
                with(binding){
                    schoolNameInfoTextView.text = item.sCHULNM
                    schoolNameAddressTextView.text = item.oRGRDNMA
                }
            }
        }

    companion object{
        private val DiffCallback = object :DiffUtil.ItemCallback<Row>(){
            override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
                return oldItem == newItem
            }

        }
    }
}