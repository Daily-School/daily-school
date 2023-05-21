package com.daily_school.daily_school.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.MealWeekRvItemBinding

class MealWeeklyRvAdapter(private val items : ArrayList<MealWeeklyModel>) : RecyclerView.Adapter<MealWeeklyRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealWeekRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val weekDate : TextView = binding.weekDayTextView
        val weekMealInfo : TextView = binding.weekMealInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealWeeklyRvAdapter.ViewHolder {

        val view = MealWeekRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealWeeklyRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.weekDate.text = item.date
        holder.weekMealInfo.text = item.mealInfo

    }

    override fun getItemCount(): Int {
        return items.size
    }


}