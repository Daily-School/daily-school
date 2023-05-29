package com.daily_school.daily_school.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealDinnerBinding
import com.daily_school.daily_school.ui.meal.breakfast.*
import com.daily_school.daily_school.ui.meal.dinner.*
import com.daily_school.daily_school.ui.meal.launch.MealLaunchTodayDialogFragment

class MealDinnerFragment : Fragment() {

    private lateinit var binding : FragmentMealDinnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_dinner, container, false)

        // 오늘의 급식 함수 호출
        todayMeal()

        // 이번주 급식 함수 호출
        weeklyMeal()

        showFragment()

        return binding.root
    }

    private fun showFragment(){
        binding.mealDinnerIcToMealDialog.setOnClickListener {
            val bottomSheet = MealDinnerTodayDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 오늘의 급식 함수
    private fun todayMeal(){

        val todayItems = ArrayList<MealDinnerTodayModel>()

        todayItems.add(MealDinnerTodayModel("날치알김치덮밥"))
        todayItems.add(MealDinnerTodayModel("유부장국"))
        todayItems.add(MealDinnerTodayModel("버섯돈육불고기"))
        todayItems.add(MealDinnerTodayModel("치커리사과무침"))
        todayItems.add(MealDinnerTodayModel("깍두기"))
        todayItems.add(MealDinnerTodayModel("메론"))

        // 데이터가 있으면 리사이클러뷰의 상태를 visible로 변환
        if (todayItems.isNotEmpty()){
            binding.mealDinnerNotionIc.visibility = View.INVISIBLE
            binding.mealDinnerNotionTextView.visibility = View.INVISIBLE
            binding.todayDinnerMealRv.visibility = View.VISIBLE

        }

        val todayDinnerRvAdapter = MealDinnerTodayRvAdapter(todayItems)

        val todayDinnerRv = binding.todayDinnerMealRv

        todayDinnerRv.adapter = todayDinnerRvAdapter

        todayDinnerRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // 이번주 급식 함수
    private fun weeklyMeal(){

        val weeklyItems = mutableListOf(
            MealDinnerWeeklyModel("5월 22일 (월)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 23일 (화)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 24일 (수)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 25일 (목)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 26일 (금)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 27일 (토)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 28일 (일)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            ),
            MealDinnerWeeklyModel("5월 29일 (월)", mutableListOf(
                MealDinnerWeeklyEachModel("날치알김치덮밥"),
                MealDinnerWeeklyEachModel("유부장국"),
                MealDinnerWeeklyEachModel("버섯돈육불고기"),
                MealDinnerWeeklyEachModel("치커리사과무침"),
                MealDinnerWeeklyEachModel("깍두기"),
                MealDinnerWeeklyEachModel("메론")
            )
            )
        )

        binding.weeklyDinnerMealRv.adapter = MealDinnerWeeklyRvAdapter(requireContext(), weeklyItems)

        binding.weeklyDinnerMealRv.layoutManager = GridLayoutManager(requireContext(), 2)

    }

}