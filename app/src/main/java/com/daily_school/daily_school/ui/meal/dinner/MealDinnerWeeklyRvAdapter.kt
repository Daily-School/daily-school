package com.daily_school.daily_school.ui.meal.dinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealBreakfastWeekRvItemBinding
import com.daily_school.daily_school.databinding.MealDinnerWeekRvItemBinding
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastWeeklyEachRvAdapter
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastWeeklyModel
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastWeeklyRvAdapter

class MealDinnerWeeklyRvAdapter(val context : Context, private val items : MutableList<MealDinnerWeeklyModel>) : RecyclerView.Adapter<MealDinnerWeeklyRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealDinnerWeekRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val dinnerWeekDate : TextView = binding.dinnerWeekDayTextView
        val dinnerWeekNotionIc : ImageView = binding.dinnerWeekNotionIc
        val dinnerWeekMealInfoTextView : TextView = binding.dinnerWeekMealInfoTextView
        val dinnerMealWeekEachInfoRv : RecyclerView = binding.dinnerMealWeekEachInfoRv
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealDinnerWeeklyRvAdapter.ViewHolder {

        val view = MealDinnerWeekRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealDinnerWeeklyRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        if (itemCount != 0){
            holder.dinnerWeekNotionIc.visibility = View.INVISIBLE
            holder.dinnerWeekMealInfoTextView.visibility = View.INVISIBLE
            holder.dinnerMealWeekEachInfoRv.visibility = View.VISIBLE
        }

        holder.dinnerMealWeekEachInfoRv.adapter = MealDinnerWeeklyEachRvAdapter(item.innerList)
        holder.dinnerMealWeekEachInfoRv.layoutManager = GridLayoutManager(context, 1)
    }

    override fun getItemCount(): Int {

        return items.size

    }

}