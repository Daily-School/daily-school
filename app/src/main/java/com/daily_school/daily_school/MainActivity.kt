package com.daily_school.daily_school

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.databinding.ActivityMainBinding
import com.daily_school.daily_school.ui.*
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        binding.mainBtn.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.mainKakaoBtn.setOnClickListener {
//            UserApiClient.instance.logout { error ->
//                if (error != null) {
//                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//                }
//                else {
//                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
//                }
//            }
//        }
//
//        binding.mainNaverBtn.setOnClickListener {
//            NaverIdLoginSDK.logout()
//        }



    }

}