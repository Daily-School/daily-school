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
import com.daily_school.daily_school.ui.schedule.*
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NeisRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
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

    private val titleArray = arrayOf("교시", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val weekDays = listOf("월", "화", "수", "목", "금")
    private val subjectList = ArrayList<ArrayList<String>>()

    private lateinit var subjectColors: Array<String>

    private val elementarySchoolType = "초등학교"
    private val middleSchoolType = "중학교"
    private val highSchoolType = "고등학교"
    private val specialSchoolType = "특수학교"
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
        dayScheduleInit(width, height)
    }

    private fun titleScheduleInit(width: Int, height : Int) {
        for (i in 0 until titleArray.size) {
            val titleTextView = TextView(requireContext())
            titleTextView.text = titleArray[i]
            titleTextView.layoutParams = LinearLayout.LayoutParams(width, height)
            titleTextView.gravity = Gravity.CENTER
            titleTextView.setBackgroundResource(R.drawable.schedule_cell_border)
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            titleTextView.setTextColor(Color.BLACK)
            titleLayout.addView(titleTextView)
        }
    }

    // 요일에 맞는 시간표를 그려 주는 함수
    private fun dayScheduleInit(width: Int, height: Int) {
        clearDayLayouts()

        for (i in 0 until subjectList.size) {
            val scheduleArray = subjectList[i]
            val consecutiveIndexes = findConsecutiveIndexes(scheduleArray)

            for (j in 0 until scheduleArray.size) {
                val dayTextView = TextView(requireContext())

                dayTextView.gravity = Gravity.CENTER
                dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                dayTextView.setTextColor(Color.BLACK)
                dayTextView.text = scheduleArray[j]

                if (scheduleArray[j].isEmpty()) {
                    dayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
                } else {
                    val matchedPair = consecutiveIndexes.find { j in it.first..it.second }
                    if (matchedPair != null) {
                        val (startIndex, endIndex) = matchedPair
                        if (j == startIndex) {
                            val span = endIndex - startIndex + 1
                            dayTextView.layoutParams = LinearLayout.LayoutParams(width, height * span)
                            dayTextView.text = scheduleArray[startIndex]
                        } else {
                            continue
                        }
                    } else {
                        dayTextView.layoutParams = LinearLayout.LayoutParams(width, height)
                    }
                }

                dayTextView.setOnClickListener {
                    val cellText = dayTextView.text.toString()
                    Log.d(TAG, "cellText: $cellText")

                    if (cellText.isNotEmpty()) {
                        showEditSubjectSheet(cellText)
                    }
                }

                val layout = when (i) {
                    0 -> mondayLayout
                    1 -> tuesdayLayout
                    2 -> wednesdayLayout
                    3 -> thursdayLayout
                    4 -> fridayLayout
                    else -> null
                }

                layout?.addView(dayTextView)
                dayTextView.setBackgroundResource(R.drawable.schedule_cell_border)
                subjectColorInit(dayTextView, dayTextView.text.toString())
            }
        }
    }

    // 중복되는 인덱스가 있는지 확인하는 함수
    private fun findConsecutiveIndexes(array: ArrayList<String>): List<Pair<Int, Int>> {
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

    // 각 시간표 클리어 함수
    private fun clearDayLayouts() {
        mondayLayout.removeAllViews()
        tuesdayLayout.removeAllViews()
        wednesdayLayout.removeAllViews()
        thursdayLayout.removeAllViews()
        fridayLayout.removeAllViews()
    }

    // 요일에 맞는 시간표를 가져 오는 함수
    private fun subjectInit() {
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
                            val userSchoolType = userInfo["schoolType"]
                            val userGradeInfo = userInfo["grade"]
                            val userClassInfo = userInfo["class"]

                            val userGrade = userGradeInfo?.toString()?.let {
                                val numericPart = it.replace(Regex("[^0-9]"), "")
                                numericPart.toIntOrNull() ?: 0
                            } ?: 0

                            val userClass = userClassInfo?.toString()?.let {
                                val numericPart = it.replace(Regex("[^0-9]"), "")
                                numericPart.toIntOrNull() ?: 0
                            } ?: 0

                            val datesForCurrentWeek = getCurrentWeekDates()

                            subjectList.clear()
                            for (i in datesForCurrentWeek.indices) {
                                subjectList.add(ArrayList())
                            }

                            for (i in datesForCurrentWeek.indices) {
                                subjectList[i].add(weekDays[i])
                                val date = datesForCurrentWeek[i]

                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Log.e(TAG, "사용자 정보 요청 실패 $error")
                                    } else if (user != null) {
                                        Log.e(TAG, "사용자 정보 요청 성공 : $user")

                                        CoroutineScope(Dispatchers.IO).launch {
                                            val response = when (userSchoolType) {
                                                elementarySchoolType -> service.elsTimetable(NeisRef.api_key, "json", 1, 8, userCityCode.toString(), userSchoolCode.toString(), userGrade.toString(), userClass.toString(), date)
                                                middleSchoolType -> service.misTimetable(NeisRef.api_key, "json", 1, 8, userCityCode.toString(), userSchoolCode.toString(), userGrade.toString(), userClass.toString(), date)
                                                highSchoolType -> service.hisTimetable(NeisRef.api_key, "json", 1, 8, userCityCode.toString(), userSchoolCode.toString(), userGrade.toString(), userClass.toString(), date)
                                                specialSchoolType -> service.spsTimetable(NeisRef.api_key, "json", 1, 8, userCityCode.toString(), userSchoolCode.toString(), userGrade.toString(), userClass.toString(), date)
                                                else -> throw IllegalArgumentException("Invalid userSchoolType") // 사용자 학교 유형이 맞지 않는 경우 예외 처리
                                            }

                                            withContext(Dispatchers.Main) {
                                                if (response.isSuccessful) {

                                                    val scheduleInfo = when (val scheduleInfoResponse = response.body()) {
                                                        is ElsScheduleInfoResponse -> scheduleInfoResponse.scheduleInfo
                                                        is MisScheduleInfoResponse -> scheduleInfoResponse.scheduleInfo
                                                        is HisScheduleInfoResponse -> scheduleInfoResponse.scheduleInfo
                                                        is SpsScheduleInfoResponse -> scheduleInfoResponse.scheduleInfo
                                                        else -> null
                                                    }

                                                    var previousFlag = false

                                                    if (scheduleInfo != null) {
                                                        if (i >= subjectList.size) {
                                                            subjectList.add(ArrayList())
                                                        }

                                                        scheduleInfo?.let {
                                                            val scheduleInfoRow = it.getOrNull(1)?.row

                                                            scheduleInfoRow?.let {
                                                                for (row in it) {
                                                                    val perio = row.pERIO
                                                                    val itrtCntnt = row.iTRTCNTNT

                                                                    if (previousFlag && perio == "1") {
                                                                        break
                                                                    }

                                                                    subjectList[i].add(itrtCntnt)
                                                                    previousFlag = true
                                                                }
                                                            }
                                                        }
                                                        while (subjectList[i].size < 10) {
                                                            subjectList[i].add("")
                                                        }

                                                        classScheduleInit()
                                                    }
                                                } else {
                                                    Log.e(TAG, "API is Fail : $response")
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
    private fun subjectColorInit(textView: TextView, subjectName: String) {
        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {
                lifecycleScope.launch {
                    try {
                        val subjectColor = firebaseManager.readSubjectData(tokenInfo.id.toString(), subjectName)

                        if (subjectColor != null) {
                            Log.d(TAG, "Subject Color : $subjectColor")
                            applySubjectColor(textView, subjectColor.toString())
                        } else {
                            Log.d(TAG, "Subject Color is Null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }
            }
        }
    }

    // 과목 값에 해당하는 과목 색상을 적용하는 함수
    private fun applySubjectColor(textView: TextView, subjectColor: String) {

        if (subjectColor != null) {
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

    // 과목 클릭 시 색상 선택 함수
    private fun showEditSubjectSheet(subject: String) {
        val bottomSheet = EditSubjectFragment()
        val bundle = Bundle()
        bundle.putString("subject", subject)
        bottomSheet.arguments = bundle
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }

    // 평일 날짜를 가져 오는 함수
    fun getCurrentWeekDates(): List<String> {
        val formatter = DateTimeFormatter.ofPattern("yyMMdd")
        val currentDate = LocalDate.now()
        val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
        val endOfWeek = currentDate.with(DayOfWeek.FRIDAY)

        val dates = mutableListOf<String>()
        var date = startOfWeek
        while (!date.isAfter(endOfWeek)) {
            dates.add(date.format(formatter))
            date = date.plusDays(1)
        }

        return dates
    }
}