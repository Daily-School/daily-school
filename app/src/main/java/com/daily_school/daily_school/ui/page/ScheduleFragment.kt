package com.daily_school.daily_school.ui.page

import FirebaseManager
import ReadDataCallback
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentPlanBinding
import com.daily_school.daily_school.databinding.FragmentScheduleBinding
import com.daily_school.daily_school.ui.plan.AddTodoActivity
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity


class ScheduleFragment : Fragment() {
    private val TAG = ScheduleFragment::class.java.simpleName

    private lateinit var binding : FragmentScheduleBinding

    val firebaseManager = FirebaseManager()

    private lateinit var titleLayout: LinearLayout
    private lateinit var mondayLayout: LinearLayout
    private lateinit var tuesdayLayout: LinearLayout
    private lateinit var wednesdayLayout: LinearLayout
    private lateinit var thursdayLayout: LinearLayout
    private lateinit var fridayLayout: LinearLayout

    // Dummy Data
    val titleArray = arrayOf("교시", "1", "2", "3", "4", "", "5", "6", "7", "8")
    val mondayArray = arrayOf("월", "수학", "국어", "영어", "영어", "", "진로", "과학", "과학", "",)
    val tuesdayArray = arrayOf("화", "국어", "국어", "수학", "수학", "", "과학", "영어", "수학", "")
    val wednesdayArray = arrayOf("수", "국어", "국어", "수학", "수학", "", "영어", "과학", "", "")
    val thursdayArray = arrayOf("목", "영어", "영어", "국어", "국어", "", "영어", "영어", "", "")
    val fridayArray = arrayOf("금", "영어", "영어", "국어", "국어", "", "영어", "영어", "", "")

    private val subjectArray = ArrayList<Pair<String, String>>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        // 과목 셋업 함수
        subjectInit()

        titleLayout = binding.titleLayout
        mondayLayout = binding.mondayLayout
        tuesdayLayout = binding.tuesdayLayout
        wednesdayLayout = binding.wednesdayLayout
        thursdayLayout = binding.thursdayLayout
        fridayLayout = binding.fridayLayout

        //color = ContextCompat.getColor(requireContext(), R.color.black)

        classScheduleInit()

        // 과목 추가 버튼 클릭 이벤트
        binding.scheduleAddArea.setOnClickListener {
            var intent = Intent(context, AddSubjectActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // 각 요일 Cell Init 함수
    private fun classScheduleInit() {

        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            58f,
            resources.displayMetrics
        ).toInt()
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            46f,
            resources.displayMetrics
        ).toInt()
        
        // 요일별 정의
        titleScheduleInit(width, height)
        mondayScheduleInit(width, height)
        tuesdayScheduleInit(width, height)
        wednesdayScheduleInit(width, height)
        thursdayScheduleInit(width, height)
        fridayScheduleInit(width, height)
    }

    private fun titleScheduleInit(width: Int, height : Int) {
        for (i in 0 until titleArray.size) {
            val titleTextView = TextView(requireContext())
            titleTextView.text = titleArray[i]
            titleTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            titleTextView.gravity = Gravity.CENTER
            titleTextView.setBackgroundResource(R.drawable.schedule_cell_left_border)
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            titleTextView.setTextColor(Color.BLACK)
            titleLayout.addView(titleTextView)
        }
    }

    private fun mondayScheduleInit(width: Int, height : Int) {
        val consecutiveIndexes = findConsecutiveIndexes(mondayArray)
        val pairArray = mutableListOf<Pair<Int, Int>>()

        for (pair in consecutiveIndexes) {
            val startIndex = pair.first
            val endIndex = pair.second
            pairArray.add(startIndex to endIndex)
        }

        for (i in 0 until mondayArray.size) {
            val mondayTextView = TextView(requireContext())
            mondayTextView.text = mondayArray[i]
            mondayTextView.gravity = Gravity.CENTER
            if (i != 5) {
                mondayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }
            mondayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            mondayTextView.setTextColor(Color.BLACK)

            val matchedPair = pairArray.find { i in it.first..it.second }
            if (i == matchedPair?.first) {
                mondayTextView.layoutParams = LinearLayout.LayoutParams(width, height, 2f)
            } else if (i == matchedPair?.second) {
                continue
            } else {
                mondayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            mondayLayout.addView(mondayTextView)
        }
    }

    private fun tuesdayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(tuesdayArray)
        val pairArray = mutableListOf<Pair<Int, Int>>()

        for (pair in consecutiveIndexes) {
            val startIndex = pair.first
            val endIndex = pair.second
            pairArray.add(startIndex to endIndex)
        }

        for (i in 0 until tuesdayArray.size) {
            val tuesdayTextView = TextView(requireContext())
            tuesdayTextView.text = tuesdayArray[i]
            tuesdayTextView.gravity = Gravity.CENTER
            if (i == 5) {
                tuesdayTextView.text = "점심"
                tuesdayTextView.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }
            else if (i != 5) {
                tuesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }
            else {
                tuesdayTextView.text = tuesdayArray[i]
            }


            tuesdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            tuesdayTextView.setTextColor(Color.BLACK)

            val matchedPair = pairArray.find { i in it.first..it.second }
            if (i == matchedPair?.first) {
                tuesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height, 2f)
            }
            else if (i == matchedPair?.second) {
                continue
            }
            else {
                tuesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            tuesdayLayout.addView(tuesdayTextView)
        }
    }

    private fun wednesdayScheduleInit(width: Int, height : Int) {
        val consecutiveIndexes = findConsecutiveIndexes(wednesdayArray)
        val pairArray = mutableListOf<Pair<Int, Int>>()

        for (pair in consecutiveIndexes) {
            val startIndex = pair.first
            val endIndex = pair.second
            pairArray.add(startIndex to endIndex)
        }

        for (i in 0 until wednesdayArray.size) {
            val wednesdayTextView = TextView(requireContext())
            wednesdayTextView.text = wednesdayArray[i]
            wednesdayTextView.gravity = Gravity.CENTER
            if (i == 5) {
                wednesdayTextView.text = "시간"
                wednesdayTextView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            }
            else if (i != 5) {
                wednesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }
            else {
                wednesdayTextView.text = tuesdayArray[i]
            }

            wednesdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            wednesdayTextView.setTextColor(Color.BLACK)

            val matchedPair = pairArray.find { i in it.first..it.second }
            if (i == matchedPair?.first) {
                wednesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height, 2f)
            }
            else if (i == matchedPair?.second) {
                continue
            }
            else {
                wednesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            wednesdayLayout.addView(wednesdayTextView)
        }
    }

    private fun thursdayScheduleInit(width: Int, height : Int) {
        val consecutiveIndexes = findConsecutiveIndexes(thursdayArray)
        val pairArray = mutableListOf<Pair<Int, Int>>()

        for (pair in consecutiveIndexes) {
            val startIndex = pair.first
            val endIndex = pair.second
            pairArray.add(startIndex to endIndex)
        }

        for (i in 0 until thursdayArray.size) {
            val thursdayTextView = TextView(requireContext())
            thursdayTextView.text = thursdayArray[i]
            thursdayTextView.gravity = Gravity.CENTER

            thursdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            thursdayTextView.setTextColor(Color.BLACK)

            if(i != 5) {
                thursdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }

            val matchedPair = pairArray.find { i in it.first..it.second }
            if (i == matchedPair?.first) {
                thursdayTextView.layoutParams = LinearLayout.LayoutParams(width, height, 2f)
            }
            else if (i == matchedPair?.second) {
                continue
            }
            else {
                thursdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            thursdayLayout.addView(thursdayTextView)
        }
    }

    private fun fridayScheduleInit(width: Int, height : Int) {
        val consecutiveIndexes = findConsecutiveIndexes(fridayArray)
        val pairArray = mutableListOf<Pair<Int, Int>>()

        for (pair in consecutiveIndexes) {
            val startIndex = pair.first
            val endIndex = pair.second
            pairArray.add(startIndex to endIndex)
        }

        for (i in 0 until fridayArray.size) {
            val fridayTextView = TextView(requireContext())
            fridayTextView.text = fridayArray[i]
            fridayTextView.gravity = Gravity.CENTER
            fridayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            fridayTextView.setTextColor(Color.BLACK)
            fridayTextView.setBackgroundResource(R.drawable.schedule_cell_right_border)

            val matchedPair = pairArray.find { i in it.first..it.second }
            if (i == matchedPair?.first) {
                fridayTextView.layoutParams = LinearLayout.LayoutParams(width, height, 2f)
            }
            else if (i == matchedPair?.second) {
                continue
            }
            else {
                fridayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            fridayLayout.addView(fridayTextView)
        }
    }



    // 중복되는 인덱스가 있는지 확인하는 함수
    private fun findConsecutiveIndexes(array: Array<String>): List<Pair<Int, Int>> {
        val consecutiveIndexes = mutableListOf<Pair<Int, Int>>()

        var startIndex = -1
        var endIndex = -1

        for (i in 0 until array.size - 1) {
            if (array[i] == array[i + 1]) {
                if (startIndex == -1) {
                    startIndex = i
                }
                endIndex = i + 1
            } else {
                if (startIndex != -1 && endIndex != -1) {
                    consecutiveIndexes.add(startIndex to endIndex)
                    startIndex = -1
                    endIndex = -1
                }
            }
        }

        if (startIndex != -1 && endIndex != -1) {
            consecutiveIndexes.add(startIndex to endIndex)
        }

        return consecutiveIndexes
    }

    private fun subjectInit() {
        subjectArray.add(Pair("Math", ""))
        subjectArray.add(Pair("Music", ""))
        subjectArray.add(Pair("science", ""))

        subjectColorInit()
    }

    // 과목 색상 셋업 함수
    private fun subjectColorInit() {
        for (index in subjectArray.indices) {
            val subject = subjectArray[index]
            val subjectName = subject.first

            firebaseManager.readSubjectData(subjectName, object : ReadDataCallback {
                override fun onSuccess(value: Any?) {
                    if (value != null) {
                        Log.d(TAG, "Subject Color: $value")
                    } else {
                        Log.d(TAG, "Subject Color is Null")
                    }
                }

                override fun onFailure(e: Exception) {
                    Log.e(TAG, "Failed to Read", e)
                }
            })
        }
    }
}