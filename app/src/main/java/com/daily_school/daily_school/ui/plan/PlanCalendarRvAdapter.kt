package com.daily_school.daily_school.ui.plan

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.PlanCalendarRvItemBinding

// PlanCalendar 연결 Adapter
class PlanCalendarRvAdapter(val context : Context, private val items : ArrayList<String>) : RecyclerView.Adapter<PlanCalendarRvAdapter.ViewHolder>(){
    private var selectedPosition: Int = -1
    private var firstPosition: Int = 0
    private var lastPosition: Int = 0

    class ViewHolder(binding : PlanCalendarRvItemBinding) : RecyclerView.ViewHolder(binding.root){
        val dayText : TextView = binding.planCalendarRvTextView
        val layout : LinearLayout = binding.planCalendarLayout
        val circleImage : ImageView = binding.planCalendarCircle
        val firstPlanTodoArea: LinearLayout = binding.firstPlanTodoArea
        val secondPlanTodoArea: LinearLayout = binding.secondPlanTodoArea
        val thirdPlanTodoArea: LinearLayout = binding.thirdPlanTodoArea
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PlanCalendarRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[holder.adapterPosition]

        // item이 null이면 text를 비워두고 아니면 날짜를 불러옴
        if(item == null){
            holder.dayText.text = ""
        }
        else{
            holder.dayText.text = item

            // 달력 첫 번째 날짜 인덱스 값
            if(item == "1") {
                firstPosition = holder.adapterPosition
            }

            // 달력 마지막 날짜 인덱스 값
            if(item == "" && lastPosition == 0 && firstPosition != 0) {
                lastPosition = holder.adapterPosition
            }
        }

        // 달력 클릭 이벤트
        holder.dayText.setOnClickListener {
            if(position > firstPosition - 1 && position < lastPosition) {
                val previousPosition = selectedPosition
                selectedPosition = holder.adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
            }
        }

        // 이미지 및 텍스트 색상 변경
        if (selectedPosition == holder.adapterPosition) {
            holder.circleImage.visibility = View.VISIBLE
            holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        else {
            holder.circleImage.visibility = View.INVISIBLE

            if ((holder.adapterPosition + 1) % 7 == 0){
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.saturday_color))
            }
            else if (holder.adapterPosition == 0 || holder.adapterPosition % 7 == 0){
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.sunday_color))
            }
            else{
                holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.gray_1))
            }
        }

        holder.firstPlanTodoArea.visibility = View.GONE
        holder.secondPlanTodoArea.visibility = View.GONE
        holder.thirdPlanTodoArea.visibility = View.GONE
    }
}