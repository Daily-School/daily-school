package com.daily_school.daily_school.ui.page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealBreakfastBinding
import com.daily_school.daily_school.ui.meal.MealCalendarFragment
import com.daily_school.daily_school.ui.meal.breakfast.*
import com.daily_school.daily_school.ui.meal.launch.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MealBreakfastFragment : Fragment() {

    private var currentDate = LocalDate.now()

    private lateinit var binding : FragmentMealBreakfastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_breakfast, container, false)

        // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
        setCurrentDate()

        // 오늘의 급식 함수 호출
        todayMeal()

        // 이번주 급식 함수 호출
        weeklyMeal()

        // 다이얼로그 프래그먼트 보여주는 함수 호출
        showFragment()

        // 캘린더를 보여주는 함수 호출
        showCalendar()

        return binding.root
    }

    // 날짜 포맷을 세팅하는 함수
    private fun loadCurrentDate(date : LocalDate) : String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일" ).withLocale(
            Locale.forLanguageTag("ko"))
        return date.format(formatter)
    }

    // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
    private fun setCurrentDate(){
        binding.mealBreakfastDateTextView.text = loadCurrentDate(currentDate)
    }

    // 다이얼로그 프래그먼트 보여주는 함수
    private fun showFragment(){
        binding.mealBreakfastIcToMealDialog.setOnClickListener {
            val bottomSheet = MealBreakfastTodayDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 캘린더를 보여주는 함수
    private fun showCalendar(){
        binding.mealBreakfastDateRectangle.setOnClickListener {
            val bottomSheet = MealCalendarFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 오늘의 급식 함수
    private fun todayMeal(){

        val todayItems = ArrayList<MealBreakfastTodayModel>()

        todayItems.add(MealBreakfastTodayModel("토마토 스파게티"))
        todayItems.add(MealBreakfastTodayModel("피클"))
        todayItems.add(MealBreakfastTodayModel("배추김치"))
        todayItems.add(MealBreakfastTodayModel("피크닉"))
        todayItems.add(MealBreakfastTodayModel("미트볼"))
        todayItems.add(MealBreakfastTodayModel("새우튀김"))

        // 데이터가 있으면 리사이클러뷰의 상태를 visible로 변환
        if (todayItems.isNotEmpty()){
            binding.mealBreakfastNotionIc.visibility = View.INVISIBLE
            binding.mealBreakfastNotionTextView.visibility = View.INVISIBLE
            binding.todayBreakfastMealRv.visibility = View.VISIBLE

        }

        val todayBreakfastRvAdapter = MealBreakfastTodayRvAdapter(todayItems)

        val todayBreakfastRv = binding.todayBreakfastMealRv

        todayBreakfastRv.adapter = todayBreakfastRvAdapter

        todayBreakfastRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // 이번주 급식 함수
    private fun weeklyMeal(){

        val weeklyItems = mutableListOf(
            MealBreakfastWeeklyModel("5월 22일 (월)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 23일 (화)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 24일 (수)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 25일 (목)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 26일 (금)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 27일 (토)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 28일 (일)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
            MealBreakfastWeeklyModel("5월 29일 (월)", mutableListOf(
                MealBreakfastWeeklyEachModel("토마토 스파게티"),
                MealBreakfastWeeklyEachModel("피클"),
                MealBreakfastWeeklyEachModel("배추김치"),
                MealBreakfastWeeklyEachModel("피크닉"),
                MealBreakfastWeeklyEachModel("미트볼"),
                MealBreakfastWeeklyEachModel("새우튀김")
            )
            ),
        )

        binding.weeklyBreakfastMealRv.adapter = MealBreakfastWeeklyRvAdapter(requireContext(), weeklyItems)

        binding.weeklyBreakfastMealRv.layoutManager = GridLayoutManager(requireContext(), 2)

    }

}