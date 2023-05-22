package com.daily_school.daily_school.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealLaunchBinding
import com.daily_school.daily_school.ui.meal.launch.*

class MealLaunchFragment : Fragment() {

    private lateinit var binding : FragmentMealLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_launch, container, false)

        // 오늘의 급식 함수 호출
        todayMeal()

        // 이번주 급식 함수 호출
        weeklyMeal()

        return binding.root
    }

    // 오늘의 급식 함수
    private fun todayMeal(){

        val todayItems = ArrayList<MealLaunchTodayModel>()

        todayItems.add(MealLaunchTodayModel("치킨마요 덮밥"))
        todayItems.add(MealLaunchTodayModel("단무지"))
        todayItems.add(MealLaunchTodayModel("탕수육"))
        todayItems.add(MealLaunchTodayModel("쿨피스"))
        todayItems.add(MealLaunchTodayModel("소고기 무국"))

        if (todayItems.isNotEmpty()){
            binding.mealLaunchNotionIc.visibility = View.INVISIBLE
            binding.mealLaunchNotionTextView.visibility = View.INVISIBLE
            binding.todayLaunchMealRv.visibility = View.VISIBLE

        }

        val todayLaunchRvAdapter = MealLaunchTodayRvAdapter(todayItems)

        val todayLaunchRv = binding.todayLaunchMealRv

        todayLaunchRv.adapter = todayLaunchRvAdapter

        todayLaunchRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // 이번주 급식 함수
    private fun weeklyMeal(){

        val weeklyItems = mutableListOf(
            MealLaunchWeeklyModel("5월 22일 (월)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 23일 (화)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 24일 (수)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 25일 (목)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 26일 (금)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 27일 (토)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 28일 (일)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 29일 (월)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
        )

        binding.weeklyLaunchMealRv.adapter = MealLaunchWeeklyRvAdapter(requireContext(), weeklyItems)

        binding.weeklyLaunchMealRv.layoutManager = GridLayoutManager(requireContext(), 2)

    }

}