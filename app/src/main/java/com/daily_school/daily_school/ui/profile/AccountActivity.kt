package com.daily_school.daily_school.ui

import FirebaseManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityAccountBinding
import com.daily_school.daily_school.ui.profile.AccountDialog
import com.daily_school.daily_school.utils.KakaoRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {

    private var TAG = AccountActivity::class.java.simpleName

    private lateinit var binding : ActivityAccountBinding

    private val firebaseManager = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)

        // 프래그먼트 화면으로 돌아가는 함수 호출
        backToProfile()

        // 유저 정보를 불러오는 함수 호출
        loadUserInfo()

        // 회원 탈퇴 기능 함수 호출
        unlinkBtn()

    }


    // 프래그먼트 화면으로 돌아가는 함수
    private fun backToProfile(){

        binding.accountIcToProfile.setOnClickListener {
            finish()
        }

    }

    // 유저 정보를 불러오는 함수
    private fun loadUserInfo(){
        KakaoSdk.init(this, KakaoRef.APP_KEY)

        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null){
                lifecycleScope.launch{
                    try {
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패 $error")
                            } else if (user != null) {
                                Log.e(TAG, "사용자 정보 요청 성공 : $user")
                                val nickname = user.kakaoAccount?.profile?.nickname
                                binding.accountCurrentAccountDynamicTextView.text = "$nickname"
                            }
                        }

                    }
                    catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    // 회원 탈퇴 기능 함수
    private fun unlinkBtn(){
        binding.accountIcToDelete.setOnClickListener {
            val dialog = AccountDialog()
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, dialog.tag)
        }
    }
}