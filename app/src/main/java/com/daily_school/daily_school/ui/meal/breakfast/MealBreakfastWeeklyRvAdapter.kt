package com.daily_school.daily_school.ui.meal.breakfast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealBreakfastWeekRvItemBinding

class MealBreakfastWeeklyRvAdapter(val context : Context, private val items : MutableList<MealBreakfastWeeklyModel>) : RecyclerView.Adapter<MealBreakfastWeeklyRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealBreakfastWeekRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val breakfastWeekDate : TextView = binding.breakfastWeekDayTextView
        val breakfastWeekNotionIc : ImageView = binding.breakfastWeekNotionIc
        val breakfastWeekMealInfoTextView : TextView = binding.breakfastWeekMealInfoTextView
        val breakfastMealWeekEachInfoRv : RecyclerView = binding.breakfastMealWeekEachInfoRv
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealBreakfastWeeklyRvAdapter.ViewHolder {

        val view = MealBreakfastWeekRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealBreakfastWeeklyRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.breakfastWeekDate.text = item.breakfastDate

        if (itemCount != 0){
            holder.breakfastWeekNotionIc.visibility = View.INVISIBLE
            holder.breakfastWeekMealInfoTextView.visibility = View.INVISIBLE
            holder.breakfastMealWeekEachInfoRv.visibility = View.VISIBLE
        }

        holder.breakfastMealWeekEachInfoRv.adapter = MealBreakfastWeeklyEachRvAdapter(item.innerList)
        holder.breakfastMealWeekEachInfoRv.layoutManager = GridLayoutManager(context, 1)
    }

    override fun getItemCount(): Int {

        return items.size

    }

}