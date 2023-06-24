package com.daily_school.daily_school.ui.schedule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
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

    private lateinit var subjectColors: Array<String>
    private var spinnerSelectPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addsubject)

        subjectColors = resources.getStringArray(R.array.subject_color_spinner_grade)

        // 배경색 지정 셋업 함수
        setupSpinnerGrade()

        // 스피너 핸들러 셋업 함수
        setupSpinnerHandler()

        // 과목명 입력 에디터 핸들러 셋업 함수
        addSubjectEditorHandler()

        // 뒤로 가기 아이콘 클릭 이벤트
        binding.addSubjectIcToPlan.setOnClickListener {
            finish()
        }

        // 선택 완료 버튼 클릭 이벤트
        binding.addSubjectSelectButton.setOnClickListener {
            selectButtonClick()
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
        binding.AddSubjectClassSpinner.adapter = spinnerAdapterGrade
    }

    // 스피너 핸들러 셋업 함수
    private fun setupSpinnerHandler() {

        binding.AddSubjectClassSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSelectPosition = position

                if (position > 0) {
                    binding.AddSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_blue)

                    if(binding.addSubjectNameEdit.text.isNotEmpty()) {
                        binding.addSubjectSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                    }
                } else {
                    binding.AddSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.AddSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
            }
        }
    }

    private fun selectButtonClick() {
        val subjectName = binding.addSubjectNameEdit.text
    }

    private fun addSubjectEditorHandler() {
        binding.addSubjectNameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    if(spinnerSelectPosition != 0) {
                        binding.addSubjectSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                    }

                    binding.addSubjectNameEdit.setBackgroundResource(R.drawable.bg_spinner_blue)
                }
                else {
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}