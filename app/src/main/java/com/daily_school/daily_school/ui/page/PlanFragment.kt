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
import com.daily_school.daily_school.ui.plan.PlanListFragment
import com.daily_school.daily_school.ui.schedule.EditSubjectFragment
import com.daily_school.daily_school.utils.KakaoRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class PlanFragment : Fragment() {
    private val TAG = PlanFragment::class.java.simpleName

    private lateinit var binding: FragmentPlanBinding

    private var selectedDate = LocalDate.now()
    private lateinit var planCalendarRvAdapter: PlanCalendarRvAdapter

    private var todoList: List<Map<String, Any>>? = null

    val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)

        // 할 일 목록 셋업 함수 호출
        todoListInit()

        // 달력 adapter 연결하는 함수 호출
        //setMonthYear()

        // 이전 달로 돌아가는 함수 호출
        backToMonth()

        // 다음 달로 넘어가는 함수 호출
        frontToMonth()

        // 할 일 추가 함수 호출
        showAddTodo()

        return binding.root
    }

    // 년월 포맷 형식을 리턴하는 함수
    private fun monthYearFromDate(date : LocalDate): String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }

    // 달력 adapter 연결하는 함수
    private fun setMonthYear(){
        binding.planMainTextView.text = monthYearFromDate(selectedDate)

        val currentYearMonth = selectedDate.toString().substringBeforeLast("-")

        val dayList = calendarArray(selectedDate)
        planCalendarRvAdapter = PlanCalendarRvAdapter(requireContext(), dayList, todoList, currentYearMonth)

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

    // 할 일 목록 셋업 함수
    private fun todoListInit() {
        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                lifecycleScope.launch {
                    try {
                        todoList = firebaseManager.readTodoListData(tokenInfo.id.toString())
                        if (todoList != null) {
                            Log.d(TAG, "TodoList : $todoList")
                            setMonthYear()
                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }
            }
        }
    }

    // 할 일 추가 함수
    private fun showAddTodo() {
        binding.addIcon.setOnClickListener {
            val intent = Intent(context, AddTodoActivity::class.java)

            val selectedDate = selectedDate.toString().substringBeforeLast("-")
            val currentSelectedDate = planCalendarRvAdapter.getSelectedDate()

            val date = "$selectedDate-$currentSelectedDate"

            intent.putExtra("selectedDate", date)

            startActivity(intent)
        }
    }
}