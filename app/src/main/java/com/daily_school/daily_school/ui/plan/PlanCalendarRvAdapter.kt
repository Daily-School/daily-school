package com.daily_school.daily_school.ui.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.PlanCalendarRvItemBinding

// PlanCalendar 연결 Adapter
class PlanCalendarRvAdapter(val context : Context, private val items : ArrayList<String>) : RecyclerView.Adapter<PlanCalendarRvAdapter.ViewHolder>(){
    private var selectedPosition: Int = -1

    class ViewHolder(binding : PlanCalendarRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val dayText : TextView = binding.planCalendarRvTextView
        val layout : ConstraintLayout = binding.planCalendarLayout
        val circleImage : ImageView = binding.planCalendarCircle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PlanCalendarRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[holder.adapterPosition] // holder.getAdapterPosition() 사용

        // item이 null이면 text를 비워두고 아니면 날짜를 불러옴
        if(item == null){
            holder.dayText.text = ""
        }
        else{
            holder.dayText.text = item
        }

        // 두 번째 영역부터 마진 조정
        if (holder.adapterPosition > 6) {
            val layoutParams = holder.layout.layoutParams as RecyclerView.LayoutParams
            layoutParams.setMargins(0, 82, 0, 0)
            holder.layout.layoutParams = layoutParams
        }

        // 달력 클릭 이벤트
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
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
    }
}