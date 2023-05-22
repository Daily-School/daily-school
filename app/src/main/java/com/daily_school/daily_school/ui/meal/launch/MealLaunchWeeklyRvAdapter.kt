package com.daily_school.daily_school.ui.meal.launch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealWeekRvItemBinding

class MealLaunchWeeklyRvAdapter(val context: Context, private val items : MutableList<MealLaunchWeeklyModel>) : RecyclerView.Adapter<MealLaunchWeeklyRvAdapter.ViewHolder>() {

    class ViewHolder(binding : MealWeekRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val launchWeekDate : TextView = binding.launchWeekDayTextView
        val launchWeekNotionIc : ImageView = binding.launchWeekNotionIc
        val launchWeekMealInfoTextView : TextView = binding.launchWeekMealInfoTextView
        val launchMealWeekEachInfoRv : RecyclerView = binding.launchMealWeekEachInfoRv
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = MealWeekRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.launchWeekDate.text = item.date

        if (itemCount != 0){
            holder.launchWeekNotionIc.visibility = View.INVISIBLE
            holder.launchWeekMealInfoTextView.visibility = View.INVISIBLE
            holder.launchMealWeekEachInfoRv.visibility = View.VISIBLE
        }

        holder.launchMealWeekEachInfoRv.adapter = MealLaunchWeeklyEachRvAdapter(item.innerList)
        holder.launchMealWeekEachInfoRv.layoutManager = GridLayoutManager(context, 1)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}