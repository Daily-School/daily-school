package com.daily_school.daily_school.ui;

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.daily_school.daily_school.MainActivity
import com.daily_school.daily_school.R
import com.daily_school.daily_school.ui.mockup.MockUpActivity

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // 3초 후에 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MockUpActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}