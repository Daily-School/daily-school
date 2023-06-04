package com.daily_school.daily_school.ui.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAddsubjectBinding

class AddSubjectActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddsubjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addsubject)

        // 뒤로 가기 아이콘 클릭 이벤트
        binding.addSubjectIcToPlan.setOnClickListener {
            finish()
        }
    }
}