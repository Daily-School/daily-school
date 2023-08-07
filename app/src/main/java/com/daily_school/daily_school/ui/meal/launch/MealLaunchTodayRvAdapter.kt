package com.daily_school.daily_school.ui.meal.launch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.core.text.htmlEncode
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealTodayRvItemBinding
import com.daily_school.daily_school.ui.meal.Row

// 오늘 식단을 연결하는 Adapter
class MealLaunchTodayRvAdapter(private val items: ArrayList<MealLaunchTodayModel>) : RecyclerView.Adapter<MealLaunchTodayRvAdapter.ViewHolder>(){

    class ViewHolder(binding : MealTodayRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val todayLaunchMealInfo : TextView = binding.launchMealTodayInfoTextView
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealLaunchTodayRvAdapter.ViewHolder {

        val view = MealTodayRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealLaunchTodayRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.todayLaunchMealInfo.text = item.todayLaunchMenu
    }

    override fun getItemCount(): Int {
        return items.size
    }


}