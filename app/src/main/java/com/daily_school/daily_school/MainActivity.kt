package com.daily_school.daily_school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.databinding.ActivityMainBinding
import com.daily_school.daily_school.ui.*
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.mainKakaoBtn.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }

        binding.mainNaverBtn.setOnClickListener {
            NaverIdLoginSDK.logout()
        }


        // 바텀 메뉴 클릭 이벤트
        binding.bottomNavigationView.setItemIconTintList(null)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.drawable.bottom_menu_home_f)
                    setOtherItemsIcon(R.id.navigation_home)

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.navigation_schedule -> {
                    item.setIcon(R.drawable.bottom_menu_schedule_f)
                    setOtherItemsIcon(R.id.navigation_schedule)

                    val intent = Intent(this, ScheduleActivity::class.java)
                    startActivity(intent)

                    true

                    true
                }
                R.id.navigation_meal_plan -> {
                    item.setIcon(R.drawable.bottom_menu_meal_plan_f)
                    setOtherItemsIcon(R.id.navigation_meal_plan)

                    val intent = Intent(this, MealActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.navigation_plan -> {
                    item.setIcon(R.drawable.bottom_menu_plan_f)
                    setOtherItemsIcon(R.id.navigation_plan)

                    val intent = Intent(this, PlanActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.navigation_profile -> {
                    item.setIcon(R.drawable.bottom_menu_profile_f)
                    setOtherItemsIcon(R.id.navigation_profile)

                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)

                    true
                }
                else -> false
            }
        }
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
}