package com.daily_school.daily_school.ui.meal.dinner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealDinnerTodayDialogRvItemBinding
import com.daily_school.daily_school.databinding.MealLaunchTodayDialogRvItemBinding
import com.daily_school.daily_school.ui.meal.launch.MealLaunchTodayDialogRvAdapter

class MealDinnerTodayDialogRvAdapter (private val items: ArrayList<MealDinnerTodayModel>) : RecyclerView.Adapter<MealDinnerTodayDialogRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealDinnerTodayDialogRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayDinnerDialogMealInfo : TextView = binding.dinnerMealTodayDialogInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealDinnerTodayDialogRvAdapter.ViewHolder {

        val view = MealDinnerTodayDialogRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealDinnerTodayDialogRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayDinnerDialogMealInfo.text = item.todayDinnerMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }

}