package com.daily_school.daily_school.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityThemeBinding

class ThemeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme)

        lightMode()

        darkMode()

        // 프래그먼트 화면으로 돌아가는 함수 호출
        backToProfile()

    }

    private fun lightMode(){
        binding.lightModeLayout.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun darkMode(){
        binding.darkModeLayout.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    // 프래그먼트 화면으로 돌아가는 함수
    private fun backToProfile(){

        binding.themeIcToProfile.setOnClickListener {
            finish()
        }

    }
}