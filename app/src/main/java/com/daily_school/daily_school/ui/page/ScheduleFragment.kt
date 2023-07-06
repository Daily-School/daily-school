package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ShareCompat.getCallingActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentScheduleBinding
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity
import com.daily_school.daily_school.ui.schedule.EditSubjectFragment
import kotlinx.coroutines.launch


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
    val mondayArray = arrayOf("월", "수학", "국어", "영어", "영어", "", "진로", "과학", "과학", "")
    val tuesdayArray = arrayOf("화", "국어", "국어", "수학", "수학", "", "과학", "영어", "수학", "")
    val wednesdayArray = arrayOf("수", "국어", "국어", "수학", "수학", "", "영어", "과학", "", "")
    val thursdayArray = arrayOf("목", "영어", "영어", "국어", "국어", "", "영어", "영어", "", "")
    val fridayArray = arrayOf("금", "영어", "영어", "국어", "국어", "", "영어", "영어", "", "")

    private val subjectArray = ArrayList<Pair<String, Any>>()

    private val subjectColorStringArray = arrayOf(
        "기본 색상",
        "라이트 블루",
        "라이트 핑크",
        "라이트 레몬",
        "라이트 바이올렛",
        "라이트 퍼플",
        "라이트 샐몬",
        "라이트 그린",
    )

    private val subjectColorArray = arrayOf(
        R.color.subject_default_color,
        R.color.subject_light_blue_color,
        R.color.subject_light_pink_color,
        R.color.subject_light_lemon_color,
        R.color.subject_light_violet_color,
        R.color.subject_light_purple_color,
        R.color.subject_light_salmon_color,
        R.color.subject_light_green_color
    )
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
        subjectColorInit()

        titleLayout = binding.titleLayout
        mondayLayout = binding.mondayLayout
        tuesdayLayout = binding.tuesdayLayout
        wednesdayLayout = binding.wednesdayLayout
        thursdayLayout = binding.thursdayLayout
        fridayLayout = binding.fridayLayout

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

            applySubjectColor(mondayArray[i], mondayTextView)

            mondayTextView.setOnClickListener {
                val cellText = mondayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                showEditSubjectSheet(cellText)
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

            applySubjectColor(tuesdayArray[i], tuesdayTextView)

            tuesdayTextView.setOnClickListener {
                val cellText = tuesdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                showEditSubjectSheet(cellText)
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
                wednesdayTextView.text = wednesdayArray[i]
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

            applySubjectColor(wednesdayArray[i], wednesdayTextView)

            wednesdayTextView.setOnClickListener {
                val cellText = wednesdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                showEditSubjectSheet(cellText)
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

            applySubjectColor(thursdayArray[i], thursdayTextView)

            thursdayTextView.setOnClickListener {
                val cellText = thursdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                showEditSubjectSheet(cellText)
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

            applySubjectColor(fridayArray[i], fridayTextView)

            fridayTextView.setOnClickListener {
                val cellText = fridayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                showEditSubjectSheet(cellText)
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
        subjectArray.add(Pair("수학", ""))
        subjectArray.add(Pair("과학", ""))
        subjectArray.add(Pair("영어", ""))
        subjectArray.add(Pair("국어", ""))
        subjectArray.add(Pair("진로", ""))
    }

    // 과목 색상 셋업 함수
    private fun subjectColorInit() {
        lifecycleScope.launch {
            try {
                for (index in subjectArray.indices) {
                    val subject = subjectArray[index]
                    val subjectName = subject.first

                    val subjectColor = firebaseManager.readSubjectData(subjectName)
                    if (subjectColor != null) {
                        Log.d(TAG, "Subject Color: $subjectColor")
                        subjectArray[index] = Pair(subjectName, subjectColor)
                    } else {
                        Log.d(TAG, "Subject Color is Null")
                    }
                }

                // 과목 색상 설정 후 각 요일 Cell 초기화
                classScheduleInit()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to Read", e)
            }
        }
    }

    // 과목 값에 해당하는 과목 색상을 적용하는 함수
    private fun applySubjectColor(subject: String, textView: TextView) {
        val matchedSubject = subjectArray.find { it.first == subject }

        if (matchedSubject != null) {
            val subjectColor = matchedSubject.second

            val color = when (subjectColor) {
                subjectColorStringArray[0] -> ContextCompat.getColor(requireContext(), subjectColorArray[0])
                subjectColorStringArray[1] -> ContextCompat.getColor(requireContext(), subjectColorArray[1])
                subjectColorStringArray[2] -> ContextCompat.getColor(requireContext(), subjectColorArray[2])
                subjectColorStringArray[3] -> ContextCompat.getColor(requireContext(), subjectColorArray[3])
                subjectColorStringArray[4] -> ContextCompat.getColor(requireContext(), subjectColorArray[4])
                subjectColorStringArray[5] -> ContextCompat.getColor(requireContext(), subjectColorArray[5])
                subjectColorStringArray[6] -> ContextCompat.getColor(requireContext(), subjectColorArray[6])
                subjectColorStringArray[7] -> ContextCompat.getColor(requireContext(), subjectColorArray[7])
                else -> Color.WHITE // 기본값으로 설정할 색상
            }
            textView.setBackgroundColor(color)
        }
    }

    private fun showEditSubjectSheet(subject: String) {
        val bottomSheet = EditSubjectFragment()
        val bundle = Bundle()
        bundle.putString("subject", subject)
        bottomSheet.arguments = bundle
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }
}