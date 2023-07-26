package com.daily_school.daily_school.ui.plan

import FirebaseManager
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.PlanCalendarRvItemBinding
import com.daily_school.daily_school.ui.meal.MealCalendarModel.Companion.selectedDate
import com.daily_school.daily_school.ui.page.PlanFragment
import com.daily_school.daily_school.ui.schedule.EditSubjectFragment
import com.daily_school.daily_school.utils.KakaoRef
import com.google.gson.Gson
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

// PlanCalendar 연결 Adapter
class PlanCalendarRvAdapter(
    val context : Context,
    private val items : ArrayList<String>,
    private val todoList: List<Map<String, Any>>?,
    private val currentDate: String) : RecyclerView.Adapter<PlanCalendarRvAdapter.ViewHolder>(){

    private val TAG = PlanCalendarRvAdapter::class.java.simpleName

    private val todoListArray: MutableList<Map<String, Any>> = mutableListOf()

    val firebaseManager = FirebaseManager()

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
        val firstPlanTodoTextView: TextView = binding.firstPlanTodoTextView
        val secondPlanTodoTextView: TextView = binding.secondPlanTodoTextView
        val thirdPlanTodoTextView: TextView = binding.thirdPlanTodoTextView
        val firstPlanTodoColorImageView: ImageView = binding.firstPlanTodoColorImageView
        val secondPlanTodoColorImageView: ImageView = binding.secondPlanTodoColorImageView
        val thirdPlanTodoColorImageView: ImageView = binding.thirdPlanTodoColorImageView
    }

    init {
        getTodoListData()
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
        else {
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
            if (item.isNotEmpty()) {
                val todoDate = "$currentDate-$item"
                val filteredTodoList = todoListArray.filter { todoItem ->
                    todoDate == todoItem["todoDate"]
                }

                if (filteredTodoList.isNotEmpty()) {
                    val bottomSheet = PlanListFragment()
                    val bundle = Bundle()
                    val gson = Gson()
                    val filteredTodoListJson = gson.toJson(filteredTodoList)
                    bundle.putString("todoInfo", filteredTodoListJson)
                    bundle.putString("todoDate", todoDate)
                    bottomSheet.arguments = bundle
                    val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                    bottomSheet.show(fragmentManager, bottomSheet.tag)
                }
            }

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

        showTodoList(holder, item)
    }

    // 할 일 목록 보여주는 함수
    private fun showTodoList(holder: ViewHolder, date: String) {
        val getDate = "$currentDate-$date"

        val filteredTodoList = todoListArray.filter { todoItem ->
            getDate == todoItem["todoDate"]
        }

        // 할 일이 있는 경우에만 보여줌
        if (filteredTodoList.isNotEmpty()) {
            holder.firstPlanTodoArea.visibility = View.VISIBLE
            setEllipsizedText(holder.firstPlanTodoTextView, filteredTodoList[0]["todoName"].toString(), 4)
            holder.firstPlanTodoColorImageView.setBackgroundResource(getTodoColorResourceId(filteredTodoList[0]["todoColor"].toString()))

            // 두 번째 할 일이 있는 경우 보여줌
            if (filteredTodoList.size >= 2) {
                holder.secondPlanTodoArea.visibility = View.VISIBLE
                setEllipsizedText(holder.secondPlanTodoTextView, filteredTodoList[1]["todoName"].toString(), 4)
                holder.secondPlanTodoColorImageView.setBackgroundResource(getTodoColorResourceId(filteredTodoList[1]["todoColor"].toString()))
            }
            else {
                // 두 번째 할 일이 없는 경우 숨김
                holder.secondPlanTodoArea.visibility = View.GONE
                holder.secondPlanTodoTextView.text = ""
            }

            // 세 번째 할 일이 있는 경우 보여줌
            if (filteredTodoList.size >= 3) {
                holder.thirdPlanTodoArea.visibility = View.VISIBLE
                setEllipsizedText(holder.thirdPlanTodoTextView, filteredTodoList[2]["todoName"].toString(), 4)
                holder.thirdPlanTodoColorImageView.setBackgroundResource(getTodoColorResourceId(filteredTodoList[2]["todoColor"].toString()))
            }
            else {
                // 세 번째 할 일이 없는 경우 숨김
                holder.thirdPlanTodoArea.visibility = View.GONE
                holder.thirdPlanTodoTextView.text = ""
            }
        }
        else {
            // 할 일이 없는 경우 모든 할 일 뷰를 숨김
            holder.firstPlanTodoArea.visibility = View.GONE
            holder.firstPlanTodoTextView.text = ""
            holder.secondPlanTodoArea.visibility = View.GONE
            holder.secondPlanTodoTextView.text = ""
            holder.thirdPlanTodoArea.visibility = View.GONE
            holder.thirdPlanTodoTextView.text = ""
        }
    }

    // todoList 데이터 저장 함수
    private fun getTodoListData() {
        todoList?.let {
            for (data in todoList) {
                val todoName = data["todoName"] as? String ?: ""
                val todoDate = data["todoDate"] as? String ?: ""
                val todoRepeat = data["todoRepeat"] as? String ?: ""
                val todoColor = data["todoColor"] as? String ?: ""

                val todoItem = mapOf(
                    "todoName" to todoName,
                    "todoDate" to todoDate,
                    "todoRepeat" to todoRepeat,
                    "todoColor" to todoColor
                )

                todoListArray.add(todoItem)
            }
        }
    }

    // 선택된 일자 호출 함수
    fun getSelectedDate(): String? {
        return if (selectedPosition >= 0 && selectedPosition < items.size) {
            items[selectedPosition]
        } else {
            null
        }
    }

    // 할 일 목록 색상 지정 함수
    private fun getTodoColorResourceId(todoColor: String): Int {
        return when (todoColor) {
            TodoColor.RED.name -> R.color.red_color
            TodoColor.BLUE.name -> R.color.main_color
            TodoColor.YELLOW.name -> R.color.yellow_color
            TodoColor.ORANGE.name -> R.color.orange_color
            TodoColor.GREEN.name -> R.color.light_green_color
            TodoColor.PURPLE.name -> R.color.pink_color
            else -> R.color.main_color
        }
    }

    // 글자 길이 지정 함수
    private fun setEllipsizedText(textView: TextView, text: String, maxLength: Int) {
        if (text.length <= maxLength) {
            textView.text = text
        } else {
            textView.text = text.take(maxLength - 1).trimEnd() + "..."
        }
    }
}