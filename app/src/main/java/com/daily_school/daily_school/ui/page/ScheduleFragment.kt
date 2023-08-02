package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentScheduleBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.ui.meal.dinner.MealDinnerTodayDialogRvAdapter
import com.daily_school.daily_school.ui.meal.dinner.MealDinnerTodayModel
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity
import com.daily_school.daily_school.ui.schedule.EditSubjectFragment
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NeisRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    private lateinit var subjectColors: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        subjectColors = resources.getStringArray(R.array.subject_color_spinner_grade)

        titleLayout = binding.titleLayout
        mondayLayout = binding.mondayLayout
        tuesdayLayout = binding.tuesdayLayout
        wednesdayLayout = binding.wednesdayLayout
        thursdayLayout = binding.thursdayLayout
        fridayLayout = binding.fridayLayout

        // 과목 셋업 함수
        subjectInit()
        subjectColorInit()

        // 과목 추가 버튼 클릭 이벤트
        binding.scheduleAddArea.setOnClickListener {
            var intent = Intent(context, AddSubjectActivity::class.java)
            startActivity(intent)
        }

        // 유저 정보를 입력시키는 함수 호출
        getSchoolInfoData()

        return binding.root
    }

    // 유저 정보를 입력시키는 함수
    private fun getSchoolInfoData(){
        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)
        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null){
                lifecycleScope.launch{
                    try {
                        val userInfo = firebaseManager.readSchoolInfoData(tokenInfo.id.toString())
                        if (userInfo != null) {
                            val userSchoolName = userInfo["schoolName"]
                            val userGrade = userInfo["grade"]
                            val userClass = userInfo["class"]
                            binding.scheduleMainTextView.text = "$userSchoolName $userGrade $userClass"

                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    }
                    catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }

    }

    // 각 요일 Cell Init 함수
    private fun classScheduleInit() {
        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            62.5f,
            resources.displayMetrics
        ).toInt()
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            52.5f,
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

    private fun mondayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(mondayArray)

        for (i in 0 until mondayArray.size) {
            val mondayTextView = TextView(requireContext())
            mondayTextView.text = mondayArray[i]
            mondayTextView.gravity = Gravity.CENTER
            mondayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            mondayTextView.setTextColor(Color.BLACK)

            val matchedPair = consecutiveIndexes.find { i in it.first..it.second }
            if (matchedPair != null) {
                val (startIndex, endIndex) = matchedPair
                if (i == startIndex) {
                    val span = endIndex - startIndex + 1
                    mondayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                    mondayTextView.text = mondayArray[startIndex]
                } else {
                    continue
                }
            } else {
                mondayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            mondayTextView.setOnClickListener {
                val cellText = mondayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                if(cellText.isNotEmpty() && !(i == 0 || i == 5)) {
                    showEditSubjectSheet(cellText)
                }
            }

            if (i != 5) {
                mondayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
                applySubjectColor(mondayArray[i], mondayTextView)
            } else {
                mondayTextView.setBackgroundResource(R.drawable.schedule_cell_left_border)
            }

            mondayLayout.addView(mondayTextView)
        }
    }

    private fun tuesdayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(tuesdayArray)

        for (i in 0 until tuesdayArray.size) {
            val tuesdayTextView = TextView(requireContext())
            tuesdayTextView.text = tuesdayArray[i]
            tuesdayTextView.gravity = Gravity.CENTER

            if (i == 5) {
                tuesdayTextView.text = "점심"
                tuesdayTextView.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            } else {
                tuesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }

            tuesdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            tuesdayTextView.setTextColor(Color.BLACK)

            val matchedPair = consecutiveIndexes.find { i in it.first..it.second }
            if (matchedPair != null) {
                val (startIndex, endIndex) = matchedPair
                if (i == startIndex) {
                    val span = endIndex - startIndex + 1
                    tuesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                    tuesdayTextView.text = tuesdayArray[startIndex]
                } else {
                    continue
                }
            } else {
                tuesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            tuesdayTextView.setOnClickListener {
                val cellText = tuesdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                if(cellText.isNotEmpty() && !(i == 0 || i == 5)) {
                    showEditSubjectSheet(cellText)
                }
            }

            // 테두리 그리기
            if (i != 5) {
                tuesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
                applySubjectColor(tuesdayArray[i], tuesdayTextView)
            }

            tuesdayLayout.addView(tuesdayTextView)
        }
    }

    private fun wednesdayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(wednesdayArray)

        for (i in 0 until wednesdayArray.size) {
            val wednesdayTextView = TextView(requireContext())
            wednesdayTextView.text = wednesdayArray[i]
            wednesdayTextView.gravity = Gravity.CENTER

            if (i == 5) {
                wednesdayTextView.text = "시간"
                wednesdayTextView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            } else {
                wednesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }

            wednesdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            wednesdayTextView.setTextColor(Color.BLACK)

            val matchedPair = consecutiveIndexes.find { i in it.first..it.second }
            if (matchedPair != null) {
                val (startIndex, endIndex) = matchedPair
                if (i == startIndex) {
                    val span = endIndex - startIndex + 1
                    wednesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                    wednesdayTextView.text = wednesdayArray[startIndex]
                } else {
                    continue
                }
            } else {
                wednesdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            wednesdayTextView.setOnClickListener {
                val cellText = wednesdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                if(cellText.isNotEmpty() && !(i == 0 || i == 5)) {
                    showEditSubjectSheet(cellText)
                }
            }

            if (i != 5) {
                wednesdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
                applySubjectColor(wednesdayArray[i], wednesdayTextView)
            }

            wednesdayLayout.addView(wednesdayTextView)
        }
    }

    private fun thursdayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(thursdayArray)

        for (i in 0 until thursdayArray.size) {
            val thursdayTextView = TextView(requireContext())
            thursdayTextView.text = thursdayArray[i]
            thursdayTextView.gravity = Gravity.CENTER

            thursdayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            thursdayTextView.setTextColor(Color.BLACK)

            if (i != 5) {
                thursdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            }

            val matchedPair = consecutiveIndexes.find { i in it.first..it.second }
            if (matchedPair != null) {
                val (startIndex, endIndex) = matchedPair
                if (i == startIndex) {
                    val span = endIndex - startIndex + 1
                    thursdayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                    thursdayTextView.text = thursdayArray[startIndex]
                } else {
                    continue
                }
            } else {
                thursdayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            thursdayTextView.setOnClickListener {
                val cellText = thursdayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                if(cellText.isNotEmpty() && !(i == 0 || i == 5)) {
                    showEditSubjectSheet(cellText)
                }
            }

            if (i != 5) {
                thursdayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
                applySubjectColor(thursdayArray[i], thursdayTextView)
            }

            thursdayLayout.addView(thursdayTextView)
        }
    }

    private fun fridayScheduleInit(width: Int, height: Int) {
        val consecutiveIndexes = findConsecutiveIndexes(fridayArray)

        for (i in 0 until fridayArray.size) {
            val fridayTextView = TextView(requireContext())
            fridayTextView.text = fridayArray[i]
            fridayTextView.gravity = Gravity.CENTER

            fridayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            fridayTextView.setTextColor(Color.BLACK)
            fridayTextView.setBackgroundResource(R.drawable.schedule_cell_right_border)

            val matchedPair = consecutiveIndexes.find { i in it.first..it.second }
            if (matchedPair != null) {
                val (startIndex, endIndex) = matchedPair
                if (i == startIndex) {
                    val span = endIndex - startIndex + 1
                    fridayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                    fridayTextView.text = fridayArray[startIndex]
                } else {
                    continue
                }
            } else {
                fridayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            }

            fridayTextView.setOnClickListener {
                val cellText = fridayTextView.text.toString()
                Log.d(TAG, "cellText: $cellText")

                if(cellText.isNotEmpty() && !(i == 0 || i == 5)) {
                    showEditSubjectSheet(cellText)
                }
            }

            fridayTextView.setBackgroundResource(R.drawable.schedule_cell_right_border)
            applySubjectColor(fridayArray[i], fridayTextView)

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

        val service = RetrofitApi.scheduleService
        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)

        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null){
                lifecycleScope.launch{
                    try {
                        val userInfo = firebaseManager.readSchoolInfoData(tokenInfo.id.toString())
                        if (userInfo != null) {
                            val userCityCode = userInfo["cityCode"]
                            val userSchoolCode = userInfo["schoolCode"]

                            val datesForCurrentWeek = getCurrentWeekDates()
                            datesForCurrentWeek.forEach { date ->
                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Log.e(TAG, "사용자 정보 요청 실패 $error")
                                    } else if (user != null) {
                                        Log.e(TAG, "사용자 정보 요청 성공 : $user")

                                        CoroutineScope(Dispatchers.IO).launch {
                                            val response = service.misScheduleData(NeisRef.api_key, "json", 1,8, userCityCode.toString(), userSchoolCode.toString(), date)
                                            withContext(Dispatchers.Main) {
                                                if(response.isSuccessful) {
                                                    val scheduleInfo = response.body()?.scheduleInfo?.get(1)?.row

                                                    if (scheduleInfo != null) {
                                                        for (row in scheduleInfo) {
                                                            val period = row.pERIO
                                                            val subject = row.iTRTCNTNT
                                                        }
                                                    }
                                                }
                                                else {
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    }
                    catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    // 과목 색상 셋업 함수
    private fun subjectColorInit() {
        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                lifecycleScope.launch {
                    try {
                        for (index in subjectArray.indices) {
                            val subject = subjectArray[index]
                            val subjectName = subject.first

                            val subjectColor = firebaseManager.readSubjectData(tokenInfo.id.toString(), subjectName)

                            if (subjectColor != null) {
                                Log.d(TAG, "Subject Color : $subjectColor")
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
        }
    }

    // 과목 값에 해당하는 과목 색상을 적용하는 함수
    private fun applySubjectColor(subject: String, textView: TextView) {
        val matchedSubject = subjectArray.find { it.first == subject }

        if (matchedSubject != null) {
            val subjectColor = matchedSubject.second
            val color = when (subjectColor) {
                subjectColors[0] -> ContextCompat.getColor(requireContext(), R.color.subject_default_color)
                subjectColors[1] -> ContextCompat.getColor(requireContext(), R.color.subject_light_blue_color)
                subjectColors[2] -> ContextCompat.getColor(requireContext(), R.color.subject_light_pink_color)
                subjectColors[3] -> ContextCompat.getColor(requireContext(), R.color.subject_light_lemon_color)
                subjectColors[4] -> ContextCompat.getColor(requireContext(), R.color.subject_light_violet_color)
                subjectColors[5] -> ContextCompat.getColor(requireContext(), R.color.subject_light_purple_color)
                subjectColors[6] -> ContextCompat.getColor(requireContext(), R.color.subject_light_salmon_color)
                subjectColors[7] -> ContextCompat.getColor(requireContext(), R.color.subject_light_green_color)
                else -> Color.WHITE // 기본값으로 설정할 색상
            }

            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(color)
            drawable.setStroke(1, ContextCompat.getColor(requireContext(), R.color.gray_7))
            textView.background = drawable
        }
    }

    private fun showEditSubjectSheet(subject: String) {
        val bottomSheet = EditSubjectFragment()
        val bundle = Bundle()
        bundle.putString("subject", subject)
        bottomSheet.arguments = bundle
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }

    fun getCurrentWeekDates(): List<String> {
        val formatter = DateTimeFormatter.ofPattern("yyMMdd")
        //val currentDate = LocalDate.now()
        val currentDate = LocalDate.of(2023, 7, 3)
        val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
        val endOfWeek = currentDate.with(DayOfWeek.SUNDAY)

        val dates = mutableListOf<String>()
        var date = startOfWeek
        while (!date.isAfter(endOfWeek)) {
            dates.add(date.format(formatter))
            date = date.plusDays(1)
        }

        return dates
    }
}