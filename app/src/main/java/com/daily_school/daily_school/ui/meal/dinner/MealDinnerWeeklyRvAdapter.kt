package com.daily_school.daily_school.ui.meal.dinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.MealDinnerWeekRvItemBinding

// 이번주 급식 날짜와 안에 리사이클러뷰를 연결하는 Adapter
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
    ): ViewHolder {

        val view = MealDinnerWeekRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealDinnerWeeklyRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.dinnerWeekDate.text = item.dinnerDate

        // itemCount가 0이 아니면 리사이클러뷰를 visible 상태로 바꿈
        if (item.innerList.size > 1){
            holder.dinnerWeekNotionIc.visibility = View.INVISIBLE
            holder.dinnerWeekMealInfoTextView.visibility = View.INVISIBLE
            holder.dinnerMealWeekEachInfoRv.visibility = View.VISIBLE
        }

        // 이번주 날짜별 급식 리사이클러 뷰를 연결하는 Adapter
        holder.dinnerMealWeekEachInfoRv.adapter = MealDinnerWeeklyEachRvAdapter(item.innerList)
        holder.dinnerMealWeekEachInfoRv.layoutManager = GridLayoutManager(context, 1)
    }

    override fun getItemCount(): Int {

        return items.size

    }

}