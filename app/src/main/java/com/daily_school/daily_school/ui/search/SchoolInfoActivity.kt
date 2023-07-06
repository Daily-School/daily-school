package com.daily_school.daily_school.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.daily_school.daily_school.MainActivity
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivitySchoolInfoBinding


var schoolInfoBool : Int = 0
var gradeBool : Int = 0
var classBool : Int = 0

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

        // 학교 검색 fragment 호출 함수 호출
        showSearchFragment()

        // 학년 스피너 셋업 함수 호출
        setupSpinnerGrade()

        // 반 스피너 셋업 함수 호출
        setupSpinnerClass()

        // 스피너 핸들러 셋업 함수 호출
        setupSpinnerHandler()

        // 학교, 학년, 반이 모두 입력이 되면 MainActivity로 이동하는 함수 호출
        changeBtn()

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
        binding.schoolInfoGradeSpinner.setSelection(spinnerAdapterGrade.count,false)
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
        binding.schoolInfoClassSpinner.setSelection(spinnerAdapterClass.count, false)
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
                binding.schoolInfoGradeSpinner.setBackgroundResource(R.drawable.bg_spinner_blue)
                gradeBool = 1
                Log.e("SchoolInfoActivity", "gradeBool true")
                if(schoolInfoBool == 1 && gradeBool == 1 && classBool == 1){
                    binding.schoolInfoSelectBtn.setBackgroundResource(R.drawable.border_main_color)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.schoolInfoGradeSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
            }
        }

        binding.schoolInfoClassSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.schoolInfoClassSpinner.setBackgroundResource(R.drawable.bg_spinner_blue)
                classBool = 1
                Log.e("SchoolInfoActivity", "classBool true")
                if(schoolInfoBool == 1 && gradeBool == 1 && classBool == 1){
                    binding.schoolInfoSelectBtn.setBackgroundResource(R.drawable.border_main_color)
                }

            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.schoolInfoClassSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
            }
        }

    }
    // SchoolNameFragment에서 SchoolInfoActivity로 데이터를 받는 함수
    fun receiveData(schoolNm : String){
        binding.schoolInfoNameEdit.setText(schoolNm)
        if(binding.schoolInfoNameEdit.text.isNotEmpty()){
            binding.schoolInfoNameEdit.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.main_color)
            binding.schoolInfoNameEdit.clearFocus()
            schoolInfoBool = 1
            if(schoolInfoBool == 1 && gradeBool == 1 && classBool == 1){
                binding.schoolInfoSelectBtn.setBackgroundResource(R.drawable.border_main_color)
            }
            Log.e("SchoolInfoActivity", "schoolInfoBool true")
        }
    }

    // 학교, 학년, 반이 모두 입력이 되면 MainActivity로 이동하는 함수
    private fun changeBtn(){

        binding.schoolInfoSelectBtn.setOnClickListener {
            if(schoolInfoBool == 1 && gradeBool == 1 && classBool == 1){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

}