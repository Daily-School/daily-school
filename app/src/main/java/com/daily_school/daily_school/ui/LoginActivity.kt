package com.daily_school.daily_school.ui

import FirebaseManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.daily_school.daily_school.MainActivity
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityLoginBinding
import com.daily_school.daily_school.ui.search.SchoolInfoActivity
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NaverRef
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val TAG = LoginActivity::class.java.simpleName

    private val firebaseManager = FirebaseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

//        NaverIdLoginSDK.initialize(
//            this,
//            NaverRef.naverClientId,
//            NaverRef.naverClientSecret,
//            NaverRef.naverClientName
//        )

        // 카카오 로그인 초기 설정
        KakaoSdk.init(this, KakaoRef.APP_KEY)

        kakaoLogin()
//        naverLogin()
    }

    private fun moveMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun moveSchoolInfoActivity(){
        startActivity(Intent(this, SchoolInfoActivity::class.java))
        finish()
    }

    // 카카오 로그인 함수
    private fun kakaoLogin(){
        // 카카오 로그인 버튼을 눌렀을 때
        binding.loginKakaoBtn.setOnClickListener {
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    lifecycleScope.launch {
                        try {
                            val userInfo = firebaseManager.readSchoolInfoData(token.idToken.toString())
                            if (userInfo != null){
                                moveMainActivity()
                            }
                            else{
                                moveSchoolInfoActivity()
                            }
                        }
                        catch (e : Exception){
                            Log.e(TAG, "Failed to Read", e)
                        }
                    }
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }
                    // 카카오톡으로 로그인 성공하면 SchoolInfoActivity로 이동
                    else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        lifecycleScope.launch {
                            try {
                                val userInfo = firebaseManager.readSchoolInfoData(token.idToken.toString())
                                if (userInfo != null){
                                    moveMainActivity()
                                }
                                else{
                                    moveSchoolInfoActivity()
                                }
                            }
                            catch (e : Exception){
                                Log.e(TAG, "Failed to Read", e)
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

//    private fun naverLogin(){
//        binding.loginNaverBtn.setOnClickListener {
//            val oauthLoginCallback = object : OAuthLoginCallback {
//                override fun onSuccess() {
//                    Log.d("test", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
//                    Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
//                    Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
//                    Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
//                    Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())
//                }
//
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//                    Log.e("test", "$errorCode $errorDescription")
//                }
//                override fun onError(errorCode: Int, message: String) {
//                    onFailure(errorCode, message)
//                }
//            }
//
//            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
//        }
//
//    }
}