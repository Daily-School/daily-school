package com.daily_school.daily_school.ui.meal.breakfast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealBreakfastWeekEachRvItemBinding
import com.daily_school.daily_school.databinding.MealWeekEachRvItemBinding

// 이번주 급식 날짜별 데이터 모델을 연결하는 Adapter
class MealBreakfastWeeklyEachRvAdapter (private val items : MutableList<MealBreakfastWeeklyEachModel>) : RecyclerView.Adapter<MealBreakfastWeeklyEachRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealBreakfastWeekEachRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val weekEachBreakfastMealInfo : TextView = binding.breakfastMealWeekEachInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = MealBreakfastWeekEachRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.weekEachBreakfastMealInfo.text = item.eachBreakfastMenu

    }

    override fun getItemCount(): Int {
        return items.size
    }

}