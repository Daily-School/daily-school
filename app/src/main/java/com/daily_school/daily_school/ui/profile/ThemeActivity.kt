package com.daily_school.daily_school.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityThemeBinding

class ThemeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme)

        binding.lightModeLayout.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeIcLightMode.visibility = View.VISIBLE
            binding.themeIcDarkMode.visibility = View.GONE

        }

        binding.darkModeLayout.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeIcLightMode.visibility = View.GONE
            binding.themeIcDarkMode.visibility = View.VISIBLE

        }

        // 프래그먼트 화면으로 돌아가는 함수 호출
        backToProfile()

    }

    // 프래그먼트 화면으로 돌아가는 함수
    private fun backToProfile(){

        binding.themeIcToProfile.setOnClickListener {
            finish()
        }

    }
}