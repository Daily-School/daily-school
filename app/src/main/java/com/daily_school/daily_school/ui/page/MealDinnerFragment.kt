package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealDinnerBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.ui.meal.MealCalendarFragment
import com.daily_school.daily_school.ui.meal.breakfast.*
import com.daily_school.daily_school.ui.meal.dinner.*
import com.daily_school.daily_school.ui.meal.launch.*
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NeisRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MealDinnerFragment : Fragment() {

    private var TAG = MealDinnerFragment::class.java.simpleName

    private var currentDate = LocalDate.now()

    private lateinit var binding : FragmentMealDinnerBinding

    private var firebaseManager = FirebaseManager()

    private var dateValue: String = ""

    private var dateTextValue: String = ""

    private lateinit var weeklyItems: MutableList<MealDinnerWeeklyModel>

    private var month : String = ""
    private var day : String = ""

    private var weeklyMonth : String = ""
    private var weeklyDay : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { _, bundle ->
            dateTextValue = bundle.getString("bundleKey")!!
            Log.d(TAG, dateTextValue)

            binding.mealDinnerDateTextView.text = dateTextValue

            loadMealDate()
            todayMeal()
            weeklyMeal()

        }

        setFragmentResultListener("monthKey") { _, bundle ->
            dateValue = bundle.getString("bundleMonthKey")!!
            Log.d(TAG, dateValue)

            todayMeal()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_dinner, container, false)

        val swipe = binding.dinnerSwipe
        swipe.setOnRefreshListener {
            loadMealDate()
            todayMeal()
            weeklyMeal()

            swipe.isRefreshing = false
        }

        // 파이어베이스에 저장 되어있는 날짜 연결 함수 호출
        loadMealDate()

        // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
        setCurrentDate()

        // 오늘의 급식 함수 호출
        todayMeal()

        // 이번주 급식 함수 호출
        weeklyMeal()

        // 다이얼로그 프래그먼트 보여주는 함수 호출
        showFragment()

        // 캘린더를 보여주는 함수 호출
        showCalendar()

        return binding.root
    }

    // 파이어베이스에 저장 되어있는 날짜 연결 함수
    private fun loadMealDate() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {

                lifecycleScope.launch {
                    try {
                        val dateInfo = firebaseManager.readDateInfoData(tokenInfo.id.toString())
                        if (dateInfo != null) {
                            val dateText = dateInfo["dateText"]
                            val mealDateInfo = dateInfo["mealDateInfo"]

                            binding.mealDinnerDateTextView.text = dateText.toString()
                            dateValue = mealDateInfo.toString()
                            dateTextValue = dateText.toString()
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    // 날짜 포맷을 세팅하는 함수
    private fun loadCurrentDate(date : LocalDate) : String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일" ).withLocale(
            Locale.forLanguageTag("ko"))
        return date.format(formatter)
    }

    // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
    private fun setCurrentDate(){
        binding.mealDinnerDateTextView.text = loadCurrentDate(currentDate)
    }

    // 다이얼로그 프래그먼트 보여주는 함수
    private fun showFragment(){
        binding.mealDinnerIcToMealDialog.setOnClickListener {
            val bottomSheet = MealDinnerTodayDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 캘린더를 보여주는 함수
    private fun showCalendar(){
        binding.mealDinnerDateRectangle.setOnClickListener {
            val bottomSheet = MealCalendarFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 오늘의 급식 함수
    private fun todayMeal() {

        val service = RetrofitApi.mealInfoService

        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
                setCurrentDate()
            } else if (tokenInfo != null) {
                lifecycleScope.launch {
                    try {
                        val userInfo = firebaseManager.readSchoolInfoData(tokenInfo.id.toString())
                        if (userInfo != null) {
                            val userCityCode = userInfo["cityCode"]
                            val userSchoolCode = userInfo["schoolCode"]

                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패 $error")
                                } else if (user != null) {
                                    Log.e(TAG, "사용자 정보 요청 성공 : $user")

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val response = service.todayMealData(
                                            "json",
                                            1,
                                            100,
                                            userCityCode.toString(),
                                            userSchoolCode.toString(),
                                            dateValue,
                                            dateValue,
                                            NeisRef.api_key
                                        )

                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {

                                                val message =
                                                    response.body()?.mealServiceDietInfo?.get(0)?.head?.get(
                                                        1
                                                    )?.rESULT?.mESSAGE.toString()

                                                Log.d(TAG, message)

                                                if (message != "정상 처리되었습니다.") {
                                                    binding.mealDinnerNotionIc.visibility =
                                                        View.VISIBLE
                                                    binding.mealDinnerNotionTextView.visibility =
                                                        View.VISIBLE
                                                    binding.todayDinnerMealRv.visibility =
                                                        View.INVISIBLE
                                                }

                                                if (response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.mMEALSCNM.toString() == "석식")
                                                {
                                                    val result =
                                                        response.body()?.mealServiceDietInfo?.get(1)?.row?.get(
                                                            0
                                                        )?.dDISHNM.toString()

                                                    val cal =
                                                        response.body()?.mealServiceDietInfo?.get(1)?.row?.get(
                                                            0
                                                        )?.cALINFO.toString()

                                                    var todayLaunchMenu = result

                                                    val arr = todayLaunchMenu.split("<br/>")

                                                    if (message != "정상 처리되었습니다.") {
                                                        binding.mealDinnerNotionIc.visibility =
                                                            View.VISIBLE
                                                        binding.mealDinnerNotionTextView.visibility =
                                                            View.VISIBLE
                                                        binding.todayDinnerMealRv.visibility =
                                                            View.INVISIBLE
                                                    } else {
                                                        val todayItems =
                                                            ArrayList<MealDinnerTodayModel>()

                                                        for (element in arr) {

                                                            todayItems.add(
                                                                MealDinnerTodayModel(
                                                                    element
                                                                )
                                                            )
                                                        }

                                                        binding.mealDinnerNotionIc.visibility =
                                                            View.INVISIBLE
                                                        binding.mealDinnerNotionTextView.visibility =
                                                            View.INVISIBLE
                                                        binding.todayDinnerMealRv.visibility =
                                                            View.VISIBLE

                                                        binding.mealDinnerCalorieTextView.text = cal

                                                        val todayDinnerRvAdapter =
                                                            MealDinnerTodayRvAdapter(todayItems)
                                                        val todayDinnerRv =
                                                            binding.todayDinnerMealRv
                                                        todayDinnerRv.adapter = todayDinnerRvAdapter
                                                        todayDinnerRv.layoutManager =
                                                            GridLayoutManager(requireContext(), 2)
                                                    }
                                                }


                                            } else {
                                                Log.e(TAG, response.code().toString())
                                            }
                                        }
                                    }
                                }
                            }


                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }

    }

    // 이번주 급식 함수
    private fun weeklyMeal() {

        val service = RetrofitApi.mealInfoService

        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
                setCurrentDate()
            } else if (tokenInfo != null) {
                lifecycleScope.launch {
                    try {
                        val userInfo = firebaseManager.readSchoolInfoData(tokenInfo.id.toString())
                        if (userInfo != null) {
                            val userCityCode = userInfo["cityCode"]
                            val userSchoolCode = userInfo["schoolCode"]

                            val dateInfo = firebaseManager.readDateInfoData(tokenInfo.id.toString())
                            if (dateInfo != null) {
                                val dateText = dateInfo["dateText"]
                                val mealDateInfo = dateInfo["mealDateInfo"]

                                dateValue = mealDateInfo.toString()
                                dateTextValue = dateText.toString()

                                val weeklyDate = dateTextValue.replace("2023년", "").trim()

                                Log.d(TAG, weeklyDate.length.toString())

                                if(weeklyDate.length == 7){
                                    month = weeklyDate.substring(0, 3)
                                    day = weeklyDate.substring(4, 6)
                                }
                                else if (weeklyDate.length == 6){
                                    month = weeklyDate.substring(0, 3)
                                    day = weeklyDate.substring(4, 5)
                                }

                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Log.e(TAG, "사용자 정보 요청 실패 $error")
                                    } else if (user != null) {
                                        Log.e(TAG, "사용자 정보 요청 성공 : $user")


                                        weeklyItems = mutableListOf(
                                            MealDinnerWeeklyModel(
                                                month + " " + day.toInt() + "일", mutableListOf(
                                                    MealDinnerWeeklyEachModel(weeklyDate)
                                                )
                                            )
                                        )
                                        var arr: List<String>

                                        CoroutineScope(Dispatchers.IO).launch {
                                            val response = service.todayMealData(
                                                "json",
                                                1,
                                                100,
                                                userCityCode.toString(),
                                                userSchoolCode.toString(),
                                                (dateValue.toInt() + 1).toString(),
                                                (dateValue.toInt() + 6).toString(),
                                                NeisRef.api_key
                                            )

                                            withContext(Dispatchers.Main) {
                                                if (response.isSuccessful) {

                                                    val message =
                                                        response.body()?.mealServiceDietInfo?.get(0)?.head?.get(1)?.rESULT?.mESSAGE.toString()

                                                    if (message != "정상 처리되었습니다.") {

                                                    } else {
                                                        val count = response.body()?.mealServiceDietInfo?.get(0)?.head?.get(0)?.listTotalCount.toString()

                                                        val arrCount = count.toInt()

                                                        for (i in 1..arrCount) {
                                                            val date = response.body()?.mealServiceDietInfo?.get(1)?.row?.get(i - 1)?.mLSVFROMYMD.toString()
                                                            Log.d(TAG, "날짜는 $date")

                                                            if (response.body()?.mealServiceDietInfo?.get(1)?.row?.get(i - 1)?.mMEALSCNM.toString() == "석식") {
                                                                val result =
                                                                    response.body()?.mealServiceDietInfo?.get(
                                                                        1
                                                                    )?.row?.get(i - 1)?.dDISHNM.toString()

                                                                val cal =
                                                                    response.body()?.mealServiceDietInfo?.get(
                                                                        1
                                                                    )?.row?.get(i - 1)?.cALINFO.toString()

                                                                arr = result.split("<br/>")

                                                                Log.d(TAG, arr.toString())

                                                                if(date.length == 7){
                                                                    weeklyMonth = date.substring(4, 6)
                                                                    weeklyDay = date.substring(6, 7)
                                                                }
                                                                else if(date.length == 8){
                                                                    weeklyMonth = date.substring(4, 6)
                                                                    weeklyDay = date.substring(6, 8)
                                                                }

                                                                val mealDay =
                                                                    weeklyMonth + "월 " + weeklyDay + "일"


                                                                when (arr.size) {
                                                                    7 -> {
                                                                        try {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[4]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[5]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[6]
                                                                                        ),
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            "내용 없음"
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        }

                                                                    }
                                                                    6 -> {
                                                                        try {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[4]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[5]
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            "내용 없음"
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        }

                                                                    }
                                                                    5 -> {
                                                                        try {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            arr[4]
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealDinnerWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealDinnerWeeklyEachModel(
                                                                                            "내용 없음"
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }

                                                    }

                                                    Log.d(TAG, weeklyItems.toString())

                                                    binding.weeklyDinnerMealRv.adapter =
                                                        MealDinnerWeeklyRvAdapter(
                                                            requireContext(),
                                                            weeklyItems
                                                        )
                                                    binding.weeklyDinnerMealRv.layoutManager =
                                                        GridLayoutManager(requireContext(), 2)

                                                } else {
                                                    Log.e(TAG, response.code().toString())
                                                }
                                            }
                                        }

                                    }
                                }

                            }

                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }

    }

}