package com.daily_school.daily_school.ui.meal.launch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealWeekEachRvItemBinding
import com.daily_school.daily_school.databinding.MealWeekRvItemBinding

class MealLaunchWeeklyEachRvAdapter (private val items : MutableList<MealLaunchWeeklyEachModel>) : RecyclerView.Adapter<MealLaunchWeeklyEachRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealWeekEachRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val weekEachLaunchMealInfo : TextView = binding.launchMealWeekEachInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = MealWeekEachRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.weekEachLaunchMealInfo.text = item.eachLaunchMenu

    }

    override fun getItemCount(): Int {
        return items.size
    }

}