package com.daily_school.daily_school.ui.meal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.MealCalendarRvItemBinding

// mealCalendar 연결 Adapter
class MealCalendarRvAdapter(val context : Context, private val items : ArrayList<String>) : RecyclerView.Adapter<MealCalendarRvAdapter.ViewHolder>(){
    class ViewHolder(binding : MealCalendarRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val dayText : TextView = binding.mealCalendarRvTextView
        val layout : ConstraintLayout = binding.mealCalendarLayout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = MealCalendarRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        // item이 null이면 text를 비워두고 아니면 날짜를 불러옴
        if(item == null){
            holder.dayText.text = ""
        }
        else{
            holder.dayText.text = item
        }

        // 평소에는 검은색, 토요일은 파랑색 일요일은 빨간색으로 표시
        if ((position + 1) % 7 == 0){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.saturday_color))
        }
        else if (position == 0 || position % 7 == 0){
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sunday_color))
        }
        else{
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.gray_1))
        }

    }
}