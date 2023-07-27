package com.daily_school.daily_school.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.HomeCalendarRvItemBinding

class HomeCalendarRvAdapter(val context : Context, private val items : ArrayList<String>, private val onItemListener: HomeOnItemListener)
    : RecyclerView.Adapter<HomeCalendarRvAdapter.ViewHolder>(){

    private var selectedPosition : Int = -1

    class ViewHolder(binding: HomeCalendarRvItemBinding) : RecyclerView.ViewHolder(binding.root){

            val dayText : TextView = binding.homeCalendarRvTextView
            val layout : ConstraintLayout = binding.homeCalendarLayout
            val circleImage : ImageView = binding.homeCalendarCircle
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = HomeCalendarRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        // item이 null이면 text를 비워두고 아니면 날짜를 불러옴
        holder.dayText.text = item

        holder.itemView.setOnClickListener {

            onItemListener.onItemClick(item, position)
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }

        // 이미지 및 텍스트 색상 변경
        if (selectedPosition == holder.adapterPosition) {
            holder.circleImage.visibility = View.VISIBLE
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        else {
            holder.circleImage.visibility = View.INVISIBLE

            if ((holder.adapterPosition + 1) % 7 == 0){
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.saturday_color))
            }
            else if (holder.adapterPosition == 0 || holder.adapterPosition % 7 == 0){
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sunday_color))
            }
            else{
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.gray_1))
            }
        }

    }
}