package com.daily_school.daily_school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragmentContainer)

        bottomNavigationView.setupWithNavController(navController)


//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 바텀 메뉴 클릭 이벤트
//        binding.bottomNavigationView.setItemIconTintList(null)
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.homeFragment -> {
//                    item.setIcon(R.drawable.bottom_menu_home_f)
//                    setOtherItemsIcon(R.id.homeFragment)
//
//                    changeFragment(HomeFragment());
//
//                    true
//                }
//                R.id.scheduleFragment -> {
//                    item.setIcon(R.drawable.bottom_menu_schedule_f)
//                    setOtherItemsIcon(R.id.scheduleFragment)
//
//                    changeFragment(ScheduleFragment());
//
//                    true
//                }
//                R.id.mealFragment -> {
//                    item.setIcon(R.drawable.bottom_menu_meal_plan_f)
//                    setOtherItemsIcon(R.id.mealFragment)
//
//                    changeFragment(MealFragment());
//
//                    true
//                }
//                R.id.planFragment -> {
//                    item.setIcon(R.drawable.bottom_menu_plan_f)
//                    setOtherItemsIcon(R.id.planFragment)
//
//                    changeFragment(PlanFragment());
//
//                    true
//                }
//                R.id.profileFragment -> {
//                    item.setIcon(R.drawable.bottom_menu_profile_f)
//                    setOtherItemsIcon(R.id.profileFragment)
//
//                    changeFragment(ProfileFragment());
//
//                    true
//                }
//                else -> false
//            }
//        }
//
//        binding.bottomNavigationView.selectedItemId = R.id.homeFragment
    }

    private fun setOtherItemsIcon(selectedItemId: Int) {
        val menu = binding.bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem.itemId != selectedItemId) {
                // 선택되지 않은 아이템 아이콘 변경
                when (menuItem.itemId) {
                    R.id.homeFragment -> menuItem.setIcon(R.drawable.bottom_menu_home_d)
                    R.id.scheduleFragment -> menuItem.setIcon(R.drawable.bottom_menu_schedule_d)
                    R.id.mealFragment -> menuItem.setIcon(R.drawable.bottom_menu_meal_plan_d)
                    R.id.planFragment -> menuItem.setIcon(R.drawable.bottom_menu_plan_d)
                    R.id.profileFragment -> menuItem.setIcon(R.drawable.bottom_menu_profile_d)
                }
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).commit()
    }
}