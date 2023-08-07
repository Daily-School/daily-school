package com.daily_school.daily_school.ui.meal.dinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealDinnerWeekEachRvItemBinding

// 이번주 급식 날짜별 데이터 모델을 연결하는 Adapter
class MealDinnerWeeklyEachRvAdapter (private val items : MutableList<MealDinnerWeeklyEachModel>) : RecyclerView.Adapter<MealDinnerWeeklyEachRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealDinnerWeekEachRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val weekEachDinnerMealInfo : TextView = binding.dinnerMealWeekEachInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = MealDinnerWeekEachRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.weekEachDinnerMealInfo.text = item.eachDinnerMenu

    }

    override fun getItemCount(): Int {
        return items.size
    }

}