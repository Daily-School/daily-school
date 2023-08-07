package com.daily_school.daily_school.ui.plan

import FirebaseManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAddtodoBinding
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity

enum class TodoColor {
    BLUE,
    YELLOW,
    RED,
    ORANGE,
    GREEN,
    PURPLE
}

enum class TodoRepeat {
    NONE,
    DAY,
    WEEK,
    MONTH,
    YEAR
}

class AddTodoActivity : AppCompatActivity() {
    private val TAG = AddTodoActivity::class.java.simpleName

    private lateinit var binding: ActivityAddtodoBinding
    private val firebaseManager = FirebaseManager()

    private var selectTodoColor: TodoColor = TodoColor.BLUE
    private var selectTodoRepeat: TodoRepeat = TodoRepeat.NONE

    private lateinit var selectedDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_addtodo)

        selectedDate = intent.getStringExtra("selectedDate") ?: "0"

        // 뒤로 가기 아이콘 클릭 이벤트
        binding.addTodoIcToPlan.setOnClickListener {
            finish()
        }

        // 선택 완료 버튼 클릭 이벤트
        binding.addTodoSelectButton.setOnClickListener {
            selectButtonClick()
        }

        // 일정 제목 입력 에디터 핸들러 셋업 함수
        addTodoEditorHandler()

        selectRepeat(binding.addTodoRepeatNoneButton, binding.addTodoRepeatNoneText, TodoRepeat.NONE)
        selectColor(binding.addTodoBlackColorCheckImage, TodoColor.BLUE)
        imageInit()
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
        binding.addTodoRepeatNoneButton.setOnClickListener {
            selectRepeat(binding.addTodoRepeatNoneButton, binding.addTodoRepeatNoneText, TodoRepeat.NONE)
        }
        binding.addTodoRepeatDayButton.setOnClickListener {
            selectRepeat(binding.addTodoRepeatDayButton, binding.addTodoRepeatDayText, TodoRepeat.DAY)
        }
        binding.addTodoRepeatWeekButton.setOnClickListener {
            selectRepeat(binding.addTodoRepeatWeekButton, binding.addTodoRepeatWeekText, TodoRepeat.WEEK)
        }
        binding.addTodoRepeatMonthButton.setOnClickListener {
            selectRepeat(binding.addTodoRepeatMonthButton, binding.addTodoRepeatMonthText, TodoRepeat.MONTH)
        }
        binding.addTodoRepeatYearButton.setOnClickListener {
            selectRepeat(binding.addTodoRepeatYearButton, binding.addTodoRepeatYearText, TodoRepeat.YEAR)
        }

        // 색상 선택 이미지
        binding.addTodoBlackColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.main_color)
        binding.addTodoBlueColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.yellow_color)
        binding.addTodoRedColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.red_color)
        binding.addTodoOrangeColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.orange_color)
        binding.addTodoLightGreenColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.light_green_color)
        binding.addTodoPinkColorImage.imageTintList = ContextCompat.getColorStateList(this, R.color.pink_color)

        // 색상 선택 이미지 클릭 리스너 설정
        binding.addTodoBlackColorImage.setOnClickListener {
            selectColor(binding.addTodoBlackColorCheckImage, TodoColor.BLUE)
        }
        binding.addTodoBlueColorImage.setOnClickListener {
            selectColor(binding.addTodoBlueColorCheckImage, TodoColor.YELLOW)
        }
        binding.addTodoRedColorImage.setOnClickListener {
            selectColor(binding.addTodoRedColorCheckImage, TodoColor.RED)
        }
        binding.addTodoOrangeColorImage.setOnClickListener {
            selectColor(binding.addTodoOrangeColorCheckImage, TodoColor.ORANGE)
        }
        binding.addTodoLightGreenColorImage.setOnClickListener {
            selectColor(binding.addTodoLightGreenColorCheckImage, TodoColor.GREEN)
        }
        binding.addTodoPinkColorImage.setOnClickListener {
            selectColor(binding.addTodoPinkColorCheckImage, TodoColor.PURPLE)
        }
    }

    // 반복 버튼 선택 함수
    private fun selectRepeat(selectedButton: ImageView, selectedText: TextView, repeat: TodoRepeat) {
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

        // 선택된 반복 업데이트
        selectTodoRepeat = repeat
    }

    // 색상 이미지 선택 함수
    private fun selectColor(selectedCheckImage: View, color: TodoColor) {
        binding.addTodoBlackColorCheckImage.visibility = View.GONE
        binding.addTodoBlueColorCheckImage.visibility = View.GONE
        binding.addTodoRedColorCheckImage.visibility = View.GONE
        binding.addTodoOrangeColorCheckImage.visibility = View.GONE
        binding.addTodoLightGreenColorCheckImage.visibility = View.GONE
        binding.addTodoPinkColorCheckImage.visibility = View.GONE

        // 선택된 원의 checkImage를 보여줌
        selectedCheckImage.visibility = View.VISIBLE

        // 선택된 색상 업데이트
        selectTodoColor = color
    }

    private fun addTodoEditorHandler() {
        binding.addTodoPlanNameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    binding.addTodoSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                    binding.addTodoPlanNameEdit.setTextColor(ContextCompat.getColorStateList(this@AddTodoActivity, R.color.black))
                }
                else {
                    binding.addTodoSelectButton.setBackgroundResource(R.drawable.radius_blue_button_transparency_30)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // 선택 완료 버튼 클릭 이벤트
    private fun selectButtonClick() {
        val todoName = binding.addTodoPlanNameEdit.text.toString()

        Log.d(TAG, "todoName: $todoName, selectTodoRepeat : $selectTodoRepeat, selectTodoColor : $selectTodoColor")

        if(todoName.isNotEmpty()) {
            firebaseManager.saveTodoListData(selectedDate, todoName, selectTodoRepeat.toString(), selectTodoColor.toString())
        }
    }
}