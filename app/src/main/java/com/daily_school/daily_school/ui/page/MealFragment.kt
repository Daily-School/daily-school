package com.daily_school.daily_school.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealBinding
import com.daily_school.daily_school.ui.meal.launch.MealLaunchFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MealFragment : Fragment() {

    private lateinit var binding : FragmentMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ViewPager 연결
        val pagerAdapter = MealLaunchFragmentAdapter(requireActivity())

        // TabLayout에 사용될 list
        val tabTitles = listOf<String>("점심", "아침", "저녁")

        // ViewPager에 필요한 Fragment 추가
        pagerAdapter.addFragment(MealLaunchFragment())
        pagerAdapter.addFragment(MealBreakfastFragment())
        pagerAdapter.addFragment(MealDinnerFragment())

        binding.mealViewPager.adapter = pagerAdapter

        binding.mealViewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })
        TabLayoutMediator(binding.tabLayout, binding.mealViewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

}