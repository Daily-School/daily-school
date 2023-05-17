package com.daily_school.daily_school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.daily_school.daily_school.databinding.ActivityMainBinding
import com.daily_school.daily_school.ui.*
import com.daily_school.daily_school.ui.page.*
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 바텀 메뉴 클릭 이벤트
        binding.bottomNavigationView.setItemIconTintList(null)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.drawable.bottom_menu_home_f)
                    setOtherItemsIcon(R.id.navigation_home)

                    changeFragment(HomeFragment());

                    true
                }
                R.id.navigation_schedule -> {
                    item.setIcon(R.drawable.bottom_menu_schedule_f)
                    setOtherItemsIcon(R.id.navigation_schedule)

                    changeFragment(ScheduleFragment());

                    true
                }
                R.id.navigation_meal_plan -> {
                    item.setIcon(R.drawable.bottom_menu_meal_plan_f)
                    setOtherItemsIcon(R.id.navigation_meal_plan)

                    changeFragment(MealFragment());

                    true
                }
                R.id.navigation_plan -> {
                    item.setIcon(R.drawable.bottom_menu_plan_f)
                    setOtherItemsIcon(R.id.navigation_plan)

                    changeFragment(PlanFragment());

                    true
                }
                R.id.navigation_profile -> {
                    item.setIcon(R.drawable.bottom_menu_profile_f)
                    setOtherItemsIcon(R.id.navigation_profile)

                    changeFragment(ProfileFragment());

                    true
                }
                else -> false
            }
        }

        binding.bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    private fun setOtherItemsIcon(selectedItemId: Int) {
        val menu = binding.bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem.itemId != selectedItemId) {
                // 선택되지 않은 아이템 아이콘 변경
                when (menuItem.itemId) {
                    R.id.navigation_home -> menuItem.setIcon(R.drawable.bottom_menu_home_d)
                    R.id.navigation_schedule -> menuItem.setIcon(R.drawable.bottom_menu_schedule_d)
                    R.id.navigation_meal_plan -> menuItem.setIcon(R.drawable.bottom_menu_meal_plan_d)
                    R.id.navigation_plan -> menuItem.setIcon(R.drawable.bottom_menu_plan_d)
                    R.id.navigation_profile -> menuItem.setIcon(R.drawable.bottom_menu_profile_d)
                }
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).commit()
    }
}