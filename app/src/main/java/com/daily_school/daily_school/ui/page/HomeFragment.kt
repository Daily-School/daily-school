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
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentHomeBinding
import com.daily_school.daily_school.utils.KakaoRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.simpleName

    private lateinit var binding : FragmentHomeBinding

    private val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

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
                            binding.homeMainTextView.text = "$userSchoolName $userGrade $userClass"

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