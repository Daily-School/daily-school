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
import androidx.viewpager2.widget.ViewPager2
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealBinding
import com.daily_school.daily_school.ui.meal.launch.MealLaunchFragmentAdapter
import com.daily_school.daily_school.utils.KakaoRef
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class MealFragment : Fragment() {

    private val TAG = MealFragment::class.java.simpleName

    private lateinit var binding : FragmentMealBinding

    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)

        // 유저 정보를 입력시키는 함수 호출
        getSchoolInfoData()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ViewPager 연결
        val pagerAdapter = MealLaunchFragmentAdapter(requireActivity())

        // TabLayout에 사용될 list
        val tabTitles = listOf<String>("점심", "아침", "저녁")

        // ViewPager에 필요한 Fragment 추가
        pagerAdapter.addFragment(MealLaunchFragment())
        pagerAdapter.addFragment(MealBreakfastFragment())
        pagerAdapter.addFragment(MealDinnerFragment())

        binding.mealViewPager.adapter = pagerAdapter

        binding.mealViewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })
        TabLayoutMediator(binding.tabLayout, binding.mealViewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()
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
                            binding.mealMainTextView.text = "$userSchoolName $userGrade $userClass"

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