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
import com.daily_school.daily_school.databinding.FragmentMealLaunchBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.ui.meal.MealCalendarFragment
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastWeeklyRvAdapter
import com.daily_school.daily_school.ui.meal.dinner.MealDinnerTodayDialogFragment
import com.daily_school.daily_school.ui.meal.launch.*
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NeisRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MealLaunchFragment : Fragment() {

    private var TAG = MealLaunchFragment::class.java.simpleName

    private var currentDate = LocalDate.now()

    private lateinit var binding: FragmentMealLaunchBinding

    private var firebaseManager = FirebaseManager()

    private var dateValue: String = ""

    private var dateTextValue: String = ""

    private var emptyString: String = ""

    private lateinit var weeklyItems: MutableList<MealLaunchWeeklyModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("requestKey") { _, bundle ->
            dateTextValue = bundle.getString("bundleKey")!!
            Log.d(TAG, dateTextValue)

            binding.mealLaunchDateTextView.text = dateTextValue

            todayMeal()
            weeklyMeal()

        }

        setFragmentResultListener("monthKey") { _, bundle ->
            dateValue = bundle.getString("bundleMonthKey")!!
            Log.d(TAG, dateValue)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_launch, container, false)

        loadMealDate()

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

                            binding.mealLaunchDateTextView.text = dateText.toString()
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
    private fun loadCurrentDate(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일").withLocale(
            Locale.forLanguageTag("ko")
        )
        return date.format(formatter)
    }

    // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
    private fun setCurrentDate() {
        binding.mealLaunchDateTextView.text = loadCurrentDate(currentDate)
    }

    // 다이얼로그 프래그먼트 보여주는 함수
    private fun showFragment() {
        binding.mealLaunchIcToMealDialog.setOnClickListener {
            val bottomSheet = MealLaunchTodayDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 캘린더를 보여주는 함수
    private fun showCalendar() {
        binding.mealLaunchDateRectangle.setOnClickListener {
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
                                                    binding.mealLaunchNotionIc.visibility =
                                                        View.VISIBLE
                                                    binding.mealLaunchNotionTextView.visibility =
                                                        View.VISIBLE
                                                    binding.todayLaunchMealRv.visibility =
                                                        View.INVISIBLE
                                                }

                                                if (response.body()?.mealServiceDietInfo?.get(1)?.row?.get(
                                                        0
                                                    )?.mMEALSCNM.toString() == "중식"
                                                ) {
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
                                                        binding.mealLaunchNotionIc.visibility =
                                                            View.VISIBLE
                                                        binding.mealLaunchNotionTextView.visibility =
                                                            View.VISIBLE
                                                        binding.todayLaunchMealRv.visibility =
                                                            View.INVISIBLE
                                                    } else {
                                                        val todayItems =
                                                            ArrayList<MealLaunchTodayModel>()

                                                        for (element in arr) {

                                                            todayItems.add(
                                                                MealLaunchTodayModel(
                                                                    element
                                                                )
                                                            )
                                                        }

                                                        binding.mealLaunchNotionIc.visibility =
                                                            View.INVISIBLE
                                                        binding.mealLaunchNotionTextView.visibility =
                                                            View.INVISIBLE
                                                        binding.todayLaunchMealRv.visibility =
                                                            View.VISIBLE

                                                        binding.mealLaunchCalorieTextView.text = cal

                                                        val todayLaunchRvAdapter =
                                                            MealLaunchTodayRvAdapter(todayItems)
                                                        val todayLaunchRv =
                                                            binding.todayLaunchMealRv
                                                        todayLaunchRv.adapter = todayLaunchRvAdapter
                                                        todayLaunchRv.layoutManager =
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

                                var weeklyDate = dateTextValue.replace("2023년", "").trim()
                                var month = weeklyDate.substring(0, 3)
                                var day = weeklyDate.substring(4, 6)

                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Log.e(TAG, "사용자 정보 요청 실패 $error")
                                    } else if (user != null) {
                                        Log.e(TAG, "사용자 정보 요청 성공 : $user")


                                        weeklyItems = mutableListOf(
                                            MealLaunchWeeklyModel(
                                                month + " " + (day.toInt()) + "일", mutableListOf(
                                                    MealLaunchWeeklyEachModel(weeklyDate)
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

                                                            if (response.body()?.mealServiceDietInfo?.get(1)?.row?.get(i - 1)?.mMEALSCNM.toString() == "중식") {
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

                                                                val month = date.substring(4, 6)
                                                                val day = date.substring(6, 8)

                                                                val mealDay =
                                                                    month + "월 " + day + "일"


                                                                when (arr.size) {
                                                                    7 -> {
                                                                        try {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[4]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[5]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[6]
                                                                                        ),
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
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
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[4]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[5]
                                                                                        ),
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
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
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[0]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[1]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[2]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[3]
                                                                                        ),
                                                                                        MealLaunchWeeklyEachModel(
                                                                                            arr[4]
                                                                                        ),
                                                                                    )
                                                                                )
                                                                            )
                                                                        } catch (e: IndexOutOfBoundsException) {
                                                                            weeklyItems.add(
                                                                                i,
                                                                                MealLaunchWeeklyModel(
                                                                                    mealDay,
                                                                                    mutableListOf(
                                                                                        MealLaunchWeeklyEachModel(
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

                                                    binding.weeklyLaunchMealRv.adapter =
                                                        MealLaunchWeeklyRvAdapter(
                                                            requireContext(),
                                                            weeklyItems
                                                        )
                                                    binding.weeklyLaunchMealRv.layoutManager =
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

//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//                Log.e(TAG, "토큰 정보 보기 실패", error)
//            } else if (tokenInfo != null) {
//
//                lifecycleScope.launch {
//                    try {
//                        val dateInfo = firebaseManager.readDateInfoData(tokenInfo.id.toString())
//                        if (dateInfo != null) {
//                            val dateText = dateInfo["dateText"]
//                            val mealDateInfo = dateInfo["mealDateInfo"]
//
//                            dateValue = mealDateInfo.toString()
//                            dateTextValue = dateText.toString()
//
//                            var weeklyDate = dateTextValue.replace("2023년","").trim()
//                            var month = weeklyDate.substring(0,3)
//                            var day = weeklyDate.substring(4,6)
//
//                            Log.d(TAG, day)
//                            weeklyItems = mutableListOf(
//                                MealLaunchWeeklyModel(month + " " + (day.toInt()) + "일", mutableListOf(
//                                    MealLaunchWeeklyEachModel(weeklyDate)
//                                ))
//                            )
//
//                            for(i in 1..4){
//                                weeklyItems.add(i, MealLaunchWeeklyModel(month + " " + (day.toInt() + i).toString() + "일", mutableListOf(
//                                    MealLaunchWeeklyEachModel(i.toString()),
//                                    MealLaunchWeeklyEachModel((i+1).toString()),
//                                    MealLaunchWeeklyEachModel((i+2).toString()),
//                                    MealLaunchWeeklyEachModel((i+3).toString()),
//                                )))
//
//                            }
//
//                            Log.d(TAG, weeklyItems[0].innerList.size.toString())
//                            Log.d(TAG, weeklyItems.toString())
//                            binding.weeklyLaunchMealRv.adapter = MealLaunchWeeklyRvAdapter(requireContext(), weeklyItems)
//                            binding.weeklyLaunchMealRv.layoutManager = GridLayoutManager(requireContext(), 2)
//                        }
//
//                    } catch (e: Exception) {
//                        Log.e(TAG, "Failed to Read", e)
//                    }
//                }
//
//            }
//        }


    }

}