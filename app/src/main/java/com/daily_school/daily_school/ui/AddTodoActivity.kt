package com.daily_school.daily_school.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAddtodoBinding
import com.daily_school.daily_school.ui.page.PlanFragment

class AddTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddtodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addtodo)

        // 뒤로 가기 아이콘 클릭 이벤트
        binding.addTodoIcToPlan.setOnClickListener {
            finish()
        }

        selectButton(binding.addTodoRepeatNoneButton, binding.addTodoRepeatNoneText)
        selectColor(binding.addTodoBlackColorCheckImage)
        imageInit()

        binding.addTodoPlanNameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    binding.addTodoSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                } else {
                    binding.addTodoSelectButton.setBackgroundResource(R.drawable.radius_blue_button_transparency_30)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // 이미지 기본 설정 함수
    private fun imageInit() {
        // 반복 버튼 이미지
        binding.addTodoRepeatNoneButton.imageTintList = ContextCompat.getColorStateList(this, R.color.main_color)
        binding.addTodoRepeatNoneText.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.addTodoRepeatDayButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatDayText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatWeekButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatWeekText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatMonthButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatMonthText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatYearButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatYearText.setTextColor(ContextCompat.getColor(this, R.color.black))

        // 반복 버튼 클릭 리스너 설정
        binding.addTodoRepeatNoneButton.setOnClickListener { selectButton(binding.addTodoRepeatNoneButton, binding.addTodoRepeatNoneText) }
        binding.addTodoRepeatDayButton.setOnClickListener { selectButton(binding.addTodoRepeatDayButton, binding.addTodoRepeatDayText) }
        binding.addTodoRepeatWeekButton.setOnClickListener { selectButton(binding.addTodoRepeatWeekButton, binding.addTodoRepeatWeekText) }
        binding.addTodoRepeatMonthButton.setOnClickListener { selectButton(binding.addTodoRepeatMonthButton, binding.addTodoRepeatMonthText)}
        binding.addTodoRepeatYearButton.setOnClickListener { selectButton(binding.addTodoRepeatYearButton, binding.addTodoRepeatYearText) }

        // 색상 선택 이미지
        binding.addTodoBlackColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.black)
        binding.addTodoRedColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.red_color)
        binding.addTodoOrangeColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.orange_color)
        binding.addTodoLightGreenColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.light_green_color)
        binding.addTodoPinkColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.pink_color)

        // 색상 선택 이미지 클릭 리스너 설정
        binding.addTodoBlackColorImage.setOnClickListener { selectColor(binding.addTodoBlackColorCheckImage) }
        binding.addTodoRedColorImage.setOnClickListener { selectColor(binding.addTodoRedColorCheckImage) }
        binding.addTodoOrangeColorImage.setOnClickListener { selectColor(binding.addTodoOrangeColorCheckImage) }
        binding.addTodoLightGreenColorImage.setOnClickListener { selectColor(binding.addTodoLightGreenColorCheckImage) }
        binding.addTodoPinkColorImage.setOnClickListener { selectColor(binding.addTodoPinkColorCheckImage) }
    }

    // 반복 버튼 선택 함수
    private fun selectButton(selectedButton: ImageView, selectedText: TextView) {
        // 선택되지 않은 버튼의 배경색과 글자색 설정
        binding.addTodoRepeatNoneButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatNoneText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatDayButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatDayText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatWeekButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatWeekText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatMonthButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatMonthText.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.addTodoRepeatYearButton.imageTintList = ContextCompat.getColorStateList(this, R.color.gray_8)
        binding.addTodoRepeatYearText.setTextColor(ContextCompat.getColor(this, R.color.black))

        // 선택된 버튼의 배경색과 글자색 설정
        selectedButton.imageTintList = ContextCompat.getColorStateList(this, R.color.main_color)
        selectedText.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    // 색상 이미지 선택 함수
    private fun selectColor(selectedCheckImage: View) {
        binding.addTodoBlackColorCheckImage.visibility = View.GONE
        binding.addTodoRedColorCheckImage.visibility = View.GONE
        binding.addTodoOrangeColorCheckImage.visibility = View.GONE
        binding.addTodoLightGreenColorCheckImage.visibility = View.GONE
        binding.addTodoPinkColorCheckImage.visibility = View.GONE

        // 선택된 원의 checkImage를 보여줌
        selectedCheckImage.visibility = View.VISIBLE
    }
}