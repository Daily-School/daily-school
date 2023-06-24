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
import com.daily_school.daily_school.R
import com.daily_school.daily_school.components.CalendarComponent
import com.daily_school.daily_school.databinding.FragmentPlanBinding
import com.daily_school.daily_school.ui.plan.AddTodoActivity
import kotlinx.coroutines.launch
import java.util.*

class PlanFragment : Fragment() {
    private val TAG = PlanFragment::class.java.simpleName

    private lateinit var binding: FragmentPlanBinding
    private lateinit var calendarComponent: CalendarComponent

    val firebaseManager = FirebaseManager()

    // 현재 년도와 월 설정
    private val calendar: Calendar = Calendar.getInstance()
    private var currentYear: Int = calendar.get(Calendar.YEAR)
    private var currentMonth: Int = calendar.get(Calendar.MONTH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)

        calendarComponent = CalendarComponent(requireContext())

        // 달력 셋업 함수
        calendarInit()
        
        // 할 일 목록 셋업 함수
        todoListInit()

        // Add 버튼 클릭 이벤트
        binding.addIcon.setOnClickListener {
            val intent = Intent(context, AddTodoActivity::class.java)
            startActivity(intent)
        }

        binding.dateRightIcon.setOnClickListener {
            currentMonth++

            if(currentMonth > 12) {
                currentMonth = 1;
                currentYear++
            }

            calendarMonthChanged(currentYear, currentMonth)
            calendarComponent.setCurrentDate(currentYear, currentMonth)
        }

        binding.dateLeftIcon.setOnClickListener {
            currentMonth--

            if(currentMonth < 1) {
                currentMonth = 12
                currentYear--
            }

            calendarMonthChanged(currentYear, currentMonth)
            calendarComponent.setCurrentDate(currentYear, currentMonth)
        }

        return binding.root
    }

    // 달력 셋업 함수
    private fun calendarInit() {
        binding.planMainTextView.text = currentYear.toString() + "년 " + currentMonth.toString() + "월"
        calendarComponent.setCurrentDate(currentYear, currentMonth)
    }

    // 할 일 목록 셋업 함수
    private fun calendarMonthChanged(year: Int, month: Int) {
        binding.planMainTextView.text = year.toString() + "년 " + month.toString() + "월"
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