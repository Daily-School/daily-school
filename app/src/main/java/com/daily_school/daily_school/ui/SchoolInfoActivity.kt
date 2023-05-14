package com.daily_school.daily_school.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivitySchoolInfoBinding


class SchoolInfoActivity : AppCompatActivity() {

    private var _binding: ActivitySchoolInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var spinnerAdapterGrade : StudentInfoSpinnerAdapter
    private lateinit var spinnerAdapterClass : StudentInfoSpinnerAdapter
    private val listOfGrade = ArrayList<StudentInfoSpinnerModel>()
    private val listOfClass = ArrayList<StudentInfoSpinnerModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivitySchoolInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 학교 검색 fragment 호출 함수
        showSearchFragment()

        // 학년 스피너 셋업 함수
        setupSpinnerGrade()

        // 반 스피너 셋업 함수
        setupSpinnerClass()

        // 스피너 핸들러 셋업 함수
        setupSpinnerHandler()
    }

    // 학교 검색 fragment 호출 함수
    private fun showSearchFragment() {
        binding.schoolInfoNameEdit.setOnClickListener {
            val bottomSheet = SchoolNameFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    // 학년 스피너 셋업 함수
    private fun setupSpinnerGrade() {
        val sGrades = resources.getStringArray(R.array.spinner_grade)

        for (i in sGrades.indices){
            val sGrade = StudentInfoSpinnerModel(sGrades[i])
            listOfGrade.add(sGrade)
        }
        spinnerAdapterGrade = StudentInfoSpinnerAdapter(this, R.layout.item_student_info_spinner, listOfGrade)
        binding.schoolInfoGradeSpinner.adapter = spinnerAdapterGrade
    }

    // 반 스피너 셋업 함수
    private fun setupSpinnerClass() {
        val sClasses = resources.getStringArray(R.array.spinner_class)

        for (i in sClasses.indices){
            val sClass = StudentInfoSpinnerModel(sClasses[i])
            listOfClass.add(sClass)
        }

        spinnerAdapterClass = StudentInfoSpinnerAdapter(this, R.layout.item_student_info_spinner, listOfClass)
        binding.schoolInfoClassSpinner.adapter = spinnerAdapterClass
    }

    // 스피너 핸들러 셋업 함수
    private fun setupSpinnerHandler() {

        binding.schoolInfoGradeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.schoolInfoClassSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.schoolInfoClassSpinner.setSelection(position, false)
                
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                //binding.schoolInfoClassSpinner.setBackgroundResource(R.drawable.bg_spinner_blue)
            }
        }

    }

}