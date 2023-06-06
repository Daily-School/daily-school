package com.daily_school.daily_school.ui.schedule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAddsubjectBinding
import com.daily_school.daily_school.ui.AddSubjectSpinnerAdapter

class AddSubjectActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddsubjectBinding

    private lateinit var spinnerAdapterGrade : AddSubjectSpinnerAdapter
    private val listOfGrade = ArrayList<AddSubjectInfoSpinnerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addsubject)

        // 배경색 지정 셋업 함수
        setupSpinnerGrade()

        // 뒤로 가기 아이콘 클릭 이벤트
        binding.addSubjectIcToPlan.setOnClickListener {
            finish()
        }
    }

    // 배경색 지정 스피너 셋업 함수
    private fun setupSpinnerGrade() {
        val sGrades = resources.getStringArray(R.array.subject_color_spinner_grade)

        for (i in sGrades.indices){
            val sGrade = AddSubjectInfoSpinnerModel(sGrades[i])
            listOfGrade.add(sGrade)
        }
        spinnerAdapterGrade = AddSubjectSpinnerAdapter(this, R.layout.item_student_info_spinner, listOfGrade)
        binding.schoolInfoClassSpinner.adapter = spinnerAdapterGrade
    }
}