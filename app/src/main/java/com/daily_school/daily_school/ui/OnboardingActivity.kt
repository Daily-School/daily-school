package com.daily_school.daily_school.ui;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.daily_school.daily_school.MainActivity
import com.daily_school.daily_school.R
import com.daily_school.daily_school.ui.mockup.MockUpActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // 할 일 목록 가이드 다시 보여 주기
        setNeverShowAgainStatus(false)

        // 3초 후에 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MockUpActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    // SharedPreferences Set 함수
    private fun setNeverShowAgainStatus(status: Boolean) {
        val sharedPreferences = getSharedPreferences("todoGuide", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("show", status)
        editor.apply()
    }
}