package com.daily_school.daily_school.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityNextBinding
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NextActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNextBinding

    private val TAG = NextActivity::class.java.simpleName

    override fun onPostCreate(savedInstanceState: Bundle?) {

        super.onPostCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_next)

        userDataList()

    }

    private fun userDataList(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                binding.txtNickname.text = user.kakaoAccount?.profile?.nickname
                binding.txtAge.text = user.kakaoAccount?.ageRange.toString()
                binding.txtEmail.text = user.kakaoAccount?.email
            }
        }
        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
            override fun onSuccess(result: NidProfileResponse) {
                binding.txtNickname.text = result.profile?.name
                binding.txtAge.text = result.profile?.birthYear
                binding.txtEmail.text = result.profile?.email
            }

            override fun onError(errorCode: Int, message: String) {

            }

            override fun onFailure(httpStatus: Int, message: String) {

            }
        })
    }
}