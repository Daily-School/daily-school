package com.daily_school.daily_school.ui.meal.breakfast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealBreakfastTodayRvItemBinding
import com.daily_school.daily_school.databinding.MealTodayRvItemBinding

// 오늘 식단을 연결하는 Adapter
class MealBreakfastTodayRvAdapter(private val items: ArrayList<MealBreakfastTodayModel>) : RecyclerView.Adapter<MealBreakfastTodayRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealBreakfastTodayRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayBreakfastMealInfo : TextView = binding.breakfastMealTodayInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealBreakfastTodayRvAdapter.ViewHolder {

        val view = MealBreakfastTodayRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealBreakfastTodayRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayBreakfastMealInfo.text = item.todayBreakfastMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }

}