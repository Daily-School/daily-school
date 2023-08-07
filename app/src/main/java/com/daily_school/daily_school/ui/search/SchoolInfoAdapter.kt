package com.daily_school.daily_school.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.SchoolNameRvItemBinding

// RecyclerView를 학교 정보 api와 연결하는 adapter
class SchoolInfoAdapter : ListAdapter<Row, SchoolInfoAdapter.SchoolInfoViewHolder>(DiffCallback) {
    interface OnItemClickListener{
        fun setOnItemClickListener(itemData: String, cityCode : String, schoolCode : String, schoolType : String, binding : SchoolNameRvItemBinding)
    }

    private lateinit var onClickListener : OnItemClickListener

    fun listItemClickFunc(pOnclick : OnItemClickListener){
        onClickListener = pOnclick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolInfoViewHolder {
        val binding = SchoolNameRvItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return SchoolInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolInfoViewHolder, position: Int) {
        holder.bind(getItem(position))

    }
    inner class SchoolInfoViewHolder(val binding : SchoolNameRvItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item : Row) {
            with(binding) {
                schoolNameInfoTextView.text = item.sCHULNM
                schoolNameAddressTextView.text = item.oRGRDNMA
            }
            // 학교 정보를 눌렀을 때 그 학교의 이름을 전달함
            if(adapterPosition!=RecyclerView.NO_POSITION){
                binding.schoolNameInfoRvArea.setOnClickListener {
                    onClickListener.setOnItemClickListener(binding.schoolNameInfoTextView.text.toString(), item.aTPTOFCDCSCCODE, item.sDSCHULCODE, item.sCHULKNDSCNM, binding)

                }
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