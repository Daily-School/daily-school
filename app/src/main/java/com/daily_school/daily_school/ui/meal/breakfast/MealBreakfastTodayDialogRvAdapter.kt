package com.daily_school.daily_school.ui.meal.breakfast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealBreakfastTodayDialogRvItemBinding

class MealBreakfastTodayDialogRvAdapter (private val items: ArrayList<MealBreakfastTodayModel>) : RecyclerView.Adapter<MealBreakfastTodayDialogRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealBreakfastTodayDialogRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayBreakfastDialogMealInfo : TextView = binding.breakfastMealTodayDialogInfoTextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealBreakfastTodayDialogRvAdapter.ViewHolder {

        val view = MealBreakfastTodayDialogRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealBreakfastTodayDialogRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayBreakfastDialogMealInfo.text = item.todayBreakfastMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }

}