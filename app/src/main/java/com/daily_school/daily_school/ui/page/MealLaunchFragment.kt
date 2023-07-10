package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealLaunchBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.ui.meal.MealCalendarFragment
import com.daily_school.daily_school.ui.meal.Row
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayDialogFragment
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MealLaunchFragment : Fragment() {

    private var TAG = MealLaunchFragment::class.java.simpleName

    private var currentDate = LocalDate.now()

    private lateinit var binding : FragmentMealLaunchBinding

    private var firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_launch, container, false)

        // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수 호출
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

    // 날짜 포맷을 세팅하는 함수
    private fun loadCurrentDate(date : LocalDate) : String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일" ).withLocale(
            Locale.forLanguageTag("ko"))
        return date.format(formatter)
    }

    // 날짜 텍스트뷰에 현재 날짜를 연결하는 함수
    private fun setCurrentDate(){
        binding.mealLaunchDateTextView.text = loadCurrentDate(currentDate)
    }

    // 다이얼로그 프래그먼트 보여주는 함수
    private fun showFragment(){
        binding.mealLaunchIcToMealDialog.setOnClickListener {
            val bottomSheet = MealLaunchTodayDialogFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 캘린더를 보여주는 함수
    private fun showCalendar(){
        binding.mealLaunchDateRectangle.setOnClickListener {
            val bottomSheet = MealCalendarFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 오늘의 급식 함수
    private fun todayMeal(){
        val service = RetrofitApi.mealInfoService

        val todayTime = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)

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

                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패 $error")
                                } else if (user != null) {
                                    Log.e(TAG, "사용자 정보 요청 성공 : $user")

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val response = service.todayMealData( "json", 1,100, userCityCode.toString(), userSchoolCode.toString(), todayTime, todayTime,NeisRef.api_key)

                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {

                                                if(response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.mMEALSCNM.toString() == "중식"){
                                                    val result = response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.dDISHNM.toString()

                                                    val cal = response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.cALINFO.toString()

                                                    var todayLaunchMenu = result

                                                    val arr = todayLaunchMenu.split("<br/>")

                                                    if (arr.size <= 1){
                                                        binding.mealLaunchNotionIc.visibility = View.VISIBLE
                                                        binding.mealLaunchNotionTextView.visibility = View.VISIBLE
                                                        binding.todayLaunchMealRv.visibility = View.INVISIBLE
                                                    }
                                                    else{
                                                        val todayItems = ArrayList<MealLaunchTodayModel>()

                                                        for (element in arr) {

                                                            todayItems.add(MealLaunchTodayModel(element))
                                                        }

                                                        binding.mealLaunchNotionIc.visibility = View.INVISIBLE
                                                        binding.mealLaunchNotionTextView.visibility = View.INVISIBLE
                                                        binding.todayLaunchMealRv.visibility = View.VISIBLE

                                                        binding.mealLaunchCalorieTextView.text = cal

                                                        val todayLaunchRvAdapter = MealLaunchTodayRvAdapter(todayItems)
                                                        val todayLaunchRv = binding.todayLaunchMealRv
                                                        todayLaunchRv.adapter = todayLaunchRvAdapter
                                                        todayLaunchRv.layoutManager = GridLayoutManager(requireContext(), 2)
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
                    }
                    catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }

    }

    // 이번주 급식 함수
    private fun weeklyMeal(){

        val weeklyItems = mutableListOf(
            MealLaunchWeeklyModel("5월 22일 (월)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 23일 (화)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 24일 (수)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 25일 (목)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 26일 (금)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 27일 (토)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 28일 (일)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
            MealLaunchWeeklyModel("5월 29일 (월)", mutableListOf(
                MealLaunchWeeklyEachModel("치킨마요 덮밥"),
                MealLaunchWeeklyEachModel("단무지"),
                MealLaunchWeeklyEachModel("탕수육"),
                MealLaunchWeeklyEachModel("쿨피스"),
                MealLaunchWeeklyEachModel("소고기 무국"),
            )
            ),
        )

        binding.weeklyLaunchMealRv.adapter = MealLaunchWeeklyRvAdapter(requireContext(), weeklyItems)

        binding.weeklyLaunchMealRv.layoutManager = GridLayoutManager(requireContext(), 2)

    }

}