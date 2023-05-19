package com.daily_school.daily_school.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAccountBinding
import com.daily_school.daily_school.ui.page.ProfileFragment

class AccountActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAccountBinding

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)

        // 프래그먼트 화면으로 돌아가는 함수 호출
        backToProfile()

    }


    // 프래그먼트 화면으로 돌아가는 함수
    private fun backToProfile(){

        binding.accountIcToProfile.setOnClickListener {
            finish()
        }

    }
}