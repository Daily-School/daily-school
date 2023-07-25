package com.daily_school.daily_school.ui.page

import FirebaseManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentProfileBinding
import com.daily_school.daily_school.ui.AccountActivity
import com.daily_school.daily_school.ui.LoginActivity
import com.daily_school.daily_school.ui.search.SchoolInfoActivity
import com.daily_school.daily_school.ui.profile.QuestionActivity
import com.daily_school.daily_school.utils.KakaoRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val TAG = ProfileFragment::class.java.simpleName

    private lateinit var binding : FragmentProfileBinding

    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        // AccountActivity 화면 이동 함수 호출
        profileToAccountActivity()

        // SchoolInfoActivity 화면 이동 함수 호출
        profileToSchoolInfo()

        // 로그아웃 기능 함수 호출
        logoutBtn()

        // QuestionActivity 화면 이동 함수 호출
        profileToQuestionActivity()

        // 유저 정보를 입력시키는 함수 호출
        getSchoolInfoData()

        return binding.root
    }

    // AccountActivity 화면 이동 함수
    private fun profileToAccountActivity(){
        binding.profileIcToAccount.setOnClickListener {
            var intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }
    }

    // QuestionActivity 화면 이동 함수
    private fun profileToQuestionActivity(){
        binding.profileIcToQuestion.setOnClickListener {
            var intent = Intent(context, QuestionActivity::class.java)
            startActivity(intent)
        }
    }

    // SchoolInfoActivity 화면 이동 함수
    private fun profileToSchoolInfo(){
        binding.profileIcToProfile.setOnClickListener {
            var intent = Intent(context, SchoolInfoActivity::class.java)
            startActivity(intent)
        }
    }

    // 로그아웃 기능 함수
    private fun logoutBtn(){
        binding.profileIcToLogout.setOnClickListener {
            KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)

            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
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
                            binding.profileMainTextView.text = "$userSchoolName $userGrade $userClass"
                            binding.profileCurrentSchoolDynamicTextView.text = "$userSchoolName $userGrade $userClass"

                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패 $error")
                                } else if (user != null) {
                                    Log.e(TAG, "사용자 정보 요청 성공 : $user")
                                    val nickname = user.kakaoAccount?.profile?.nickname
                                    binding.profileNicknameTextView.text = "$nickname"
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