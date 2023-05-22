package com.daily_school.daily_school.ui.meal.launch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealTodayRvItemBinding
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayModel


class MealLaunchTodayRvAdapter(private val items: ArrayList<MealLaunchTodayModel>) : RecyclerView.Adapter<MealLaunchTodayRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealTodayRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayLaunchMealInfo : TextView = binding.launchMealTodayInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = MealTodayRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.todayLaunchMealInfo.text = item.todayLaunchMenu

    }

    override fun getItemCount(): Int {
        return items.size
    }


}