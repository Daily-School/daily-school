package com.daily_school.daily_school.ui.meal.launch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealLaunchTodayDialogRvItemBinding

class MealLaunchTodayDialogRvAdapter (private val items: ArrayList<MealLaunchTodayModel>) : RecyclerView.Adapter<MealLaunchTodayDialogRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealLaunchTodayDialogRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayLaunchDialogMealInfo : TextView = binding.launchMealTodayDialogInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealLaunchTodayDialogRvAdapter.ViewHolder {

        val view = MealLaunchTodayDialogRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealLaunchTodayDialogRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayLaunchDialogMealInfo.text = item.todayLaunchMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }

}