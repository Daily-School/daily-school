package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentHomeBinding
import com.daily_school.daily_school.home.HomeCalendarFragment
import com.daily_school.daily_school.utils.KakaoRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.simpleName

    private lateinit var binding : FragmentHomeBinding

    private var currentDate = LocalDate.now()

    private val firebaseManager = FirebaseManager()

    private var midDateValue : String = ""
    private var finalDateValue : String = ""

    private var todayDateValue : String? = ""

    private var midResultValue : String? = ""
    private var finalResultValue : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("MidDateKey") { _, bundle ->
            midDateValue = bundle.getString("MidDateKey1")!!
            Log.e(TAG, midDateValue)
            changeMidTextView()

        }

        setFragmentResultListener("FinalDateKey") { _, bundle ->
            finalDateValue = bundle.getString("FinalDateKey1")!!
            Log.e(TAG, finalDateValue)

            changeFinalTextView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val swipe = binding.homeSwipeLayout
        swipe.setOnRefreshListener {

            changeFirebaseMidTextView()

            changeFirebaseFinalTextView()

            swipe.isRefreshing = false
        }

        changeFirebaseMidTextView()

        changeFirebaseFinalTextView()

        // 유저 정보를 입력시키는 함수 호출
        getSchoolInfoData()

        setCurrentDate()

        showMidCalendar()

        showFinalCalendar()

        return binding.root

    }

    private fun changeMidTextView(){
        todayDateValue = loadTodayDate(currentDate)
        midResultValue = (midDateValue.toInt() - todayDateValue!!.toInt()).toString()
        if(midResultValue!!.toInt() < 0){
            binding.homeMidDDayTextView.visibility = View.VISIBLE
            binding.homeMidDayTextView.visibility = View.INVISIBLE
            binding.homeMidDDayTextView.text = "D+" + abs(midResultValue!!.toInt())
        }
        else{
            binding.homeMidDDayTextView.visibility = View.VISIBLE
            binding.homeMidDayTextView.visibility = View.INVISIBLE
            binding.homeMidDDayTextView.text = "D-" + midResultValue!!
        }
    }

    private fun changeFinalTextView(){
        todayDateValue = loadTodayDate(currentDate)
        finalResultValue = (finalDateValue.toInt() - todayDateValue!!.toInt()).toString()
        if(finalResultValue!!.toInt() < 0){
            binding.homeFinalDDayTextView.visibility = View.VISIBLE
            binding.homeFinalDayTextView.visibility = View.INVISIBLE
            binding.homeFinalDDayTextView.text = "D+" + abs(finalResultValue!!.toInt())
        }
        else{
            binding.homeFinalDDayTextView.visibility = View.VISIBLE
            binding.homeFinalDayTextView.visibility = View.INVISIBLE
            binding.homeFinalDDayTextView.text = "D-" + finalResultValue!!
        }
    }

    private fun changeFirebaseMidTextView(){
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {

                lifecycleScope.launch {
                    try {
                        val dateInfo = firebaseManager.readDDayDateInfoData(tokenInfo.id.toString())
                        if (dateInfo != null) {
                            val midDateText = dateInfo["midDay"].toString()
                            todayDateValue = loadTodayDate(currentDate)
                            midResultValue = (midDateText.toInt() - todayDateValue!!.toInt()).toString()
                            if(midResultValue!!.toInt() < 0){
                                binding.homeMidDDayTextView.visibility = View.VISIBLE
                                binding.homeMidDayTextView.visibility = View.INVISIBLE
                                binding.homeMidDDayTextView.text = "D+" + abs(midResultValue!!.toInt())
                            }
                            else{
                                binding.homeMidDDayTextView.visibility = View.VISIBLE
                                binding.homeMidDayTextView.visibility = View.INVISIBLE
                                binding.homeMidDDayTextView.text = "D-" + midResultValue!!
                            }


                            Log.d(TAG, midResultValue!!)
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    private fun changeFirebaseFinalTextView(){

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            } else if (tokenInfo != null) {

                lifecycleScope.launch {
                    try {
                        val dateInfo = firebaseManager.readDDayDateInfoData(tokenInfo.id.toString())
                        if (dateInfo != null) {
                            val finalDateText = dateInfo["finalDay"].toString()
                            todayDateValue = loadTodayDate(currentDate)
                            finalResultValue = (finalDateText.toInt() - todayDateValue!!.toInt()).toString()
                            if(finalResultValue!!.toInt() < 0){
                                binding.homeFinalDDayTextView.visibility = View.VISIBLE
                                binding.homeFinalDayTextView.visibility = View.INVISIBLE
                                binding.homeFinalDDayTextView.text = "D+" + abs(finalResultValue!!.toInt())
                            }
                            else{
                                binding.homeFinalDDayTextView.visibility = View.VISIBLE
                                binding.homeFinalDayTextView.visibility = View.INVISIBLE
                                binding.homeFinalDDayTextView.text = "D-" + finalResultValue!!
                            }

                            Log.d(TAG, finalResultValue!!)
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
        binding.homeTodayTextView.text = loadCurrentDate(currentDate)
    }

    // 날짜 포맷을 세팅하는 함수
    private fun loadTodayDate(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(
            Locale.forLanguageTag("ko")
        )
        return date.format(formatter)
    }

    private fun showMidCalendar(){
        binding.homeIcMidToCalendar.setOnClickListener {
            val bottomSheet = HomeCalendarFragment("중간고사",0)
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    private fun showFinalCalendar(){
        binding.homeIcFinalToCalendar.setOnClickListener {
            val bottomSheet = HomeCalendarFragment("기말고사",1)
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){


        val ft : FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()

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
                            binding.homeMainTextView.text = "$userSchoolName $userGrade $userClass"

                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패 $error")
                                } else if (user != null) {
                                    Log.e(TAG, "사용자 정보 요청 성공 : $user")
                                    val nickname = user.kakaoAccount?.profile?.nickname
                                    binding.homeNicknameTextView.text = "$nickname"
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

}