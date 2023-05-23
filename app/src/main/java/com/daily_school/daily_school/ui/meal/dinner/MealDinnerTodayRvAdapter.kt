package com.daily_school.daily_school.ui.meal.dinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealDinnerTodayRvItemBinding

// 오늘 식단을 연결하는 Adapter
class MealDinnerTodayRvAdapter (private val items: ArrayList<MealDinnerTodayModel>) : RecyclerView.Adapter<MealDinnerTodayRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealDinnerTodayRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayDinnerMealInfo : TextView = binding.dinnerMealTodayInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealDinnerTodayRvAdapter.ViewHolder {

        val view = MealDinnerTodayRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealDinnerTodayRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayDinnerMealInfo.text = item.todayDinnerMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }
}