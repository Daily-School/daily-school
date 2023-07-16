package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentPlanBinding
import com.daily_school.daily_school.ui.plan.AddTodoActivity
import com.daily_school.daily_school.ui.plan.PlanCalendarRvAdapter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class PlanFragment : Fragment() {
    private val TAG = PlanFragment::class.java.simpleName

    private lateinit var binding: FragmentPlanBinding

    private var selectedDate = LocalDate.now()

    val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)

        // 할 일 목록 셋업 함수
        todoListInit()

        // Add 버튼 클릭 이벤트
        binding.addIcon.setOnClickListener {
            val intent = Intent(context, AddTodoActivity::class.java)
            startActivity(intent)
        }

        // 달력 adapter 연결하는 함수 호출
        setMonthYear()

        // 이전 달로 돌아가는 함수 호출
        backToMonth()

        // 다음 달로 넘어가는 함수 호출
        frontToMonth()

        return binding.root
    }

    // 년월 포맷 형식을 리턴하는 함수
    private fun monthYearFromDate(date : LocalDate): String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }

    // 할 일 목록 셋업 함수
    private fun calendarMonthChanged(year: Int, month: Int) {
        binding.planMainTextView.text = year.toString() + "년 " + month.toString() + "월"
    }

    // 달력 adapter 연결하는 함수
    private fun setMonthYear(){
        binding.planMainTextView.text = monthYearFromDate(selectedDate)

        val dayList = calendarArray(selectedDate)
        val planCalendarRvAdapter = PlanCalendarRvAdapter(requireContext(), dayList)

        val planCalendarRv = binding.planCalendarRv

        planCalendarRv.adapter = planCalendarRvAdapter

        planCalendarRv.layoutManager = GridLayoutManager(requireContext(), 7)
    }

    // array에 실제 날짜를 연결하는 함수
    private fun calendarArray(date : LocalDate) : ArrayList<String> {
        val dayList = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val lastDay : Int = yearMonth.lengthOfMonth()
        val firstDay : LocalDate = selectedDate.withDayOfMonth(1)

        val dayOfWeek : Int = firstDay.dayOfWeek.value

        for (i : Int in 1..42){
            if (i <= dayOfWeek || i > lastDay + dayOfWeek){
                dayList.add("")
            }
            else{
                dayList.add((i-dayOfWeek).toString())
            }
        }

        return dayList

    }

    // 이전 달로 돌아가는 함수
    private fun backToMonth(){
        binding.dateLeftIcon.setOnClickListener {

            selectedDate = selectedDate.minusMonths(1)
            setMonthYear()
        }
    }

    // 다음 달로 넘어가는 함수
    private fun frontToMonth(){
        binding.dateRightIcon.setOnClickListener {

            selectedDate = selectedDate.plusMonths(1)
            setMonthYear()
        }
    }

    private fun todoListInit() {
        lifecycleScope.launch {
            try {
                val todoList = firebaseManager.readTodoListData()
                if (todoList != null) {
                    Log.d(TAG, "TodoList : $todoList")
                } else {
                    Log.d(TAG, "TodoList is Null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to Read", e)
            }
        }
    }
}