package com.daily_school.daily_school.ui.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentProfileBinding
import com.daily_school.daily_school.ui.AccountActivity
import com.daily_school.daily_school.ui.search.SchoolInfoActivity
import com.daily_school.daily_school.ui.profile.QuestionActivity

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

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

        // QuestionActivity 화면 이동 함수 호출
        profileToQuestionActivity()

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

}