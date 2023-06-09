package com.daily_school.daily_school.ui.mockup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentSecondMockUpBinding
import com.daily_school.daily_school.ui.LoginActivity

class SecondMockUpFragment : Fragment() {

    private lateinit var binding : FragmentSecondMockUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_mock_up, container, false)

        // 프래그먼트 화면 이동 함수 호출
        moveFragment()

        // 로그인 액티비티로 이동하는 함수 호출
        fragmentToLogin()

        return binding.root
    }

    // 프래그먼트 화면 이동 함수
    private fun moveFragment(){

        binding.secondMockUpIcFirstRolling.setOnClickListener {
            it.findNavController().navigate(R.id.action_secondMockUpFragment_to_firstMockUpFragment)
        }

        binding.secondMockUpIcThirdRolling.setOnClickListener {
            it.findNavController().navigate(R.id.action_secondMockUpFragment_to_thirdMockUpFragment)
        }

        binding.secondMockUpBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_secondMockUpFragment_to_thirdMockUpFragment)
        }
    }

    // 로그인 액티비티로 이동하는 함수
    private fun fragmentToLogin(){
        binding.secondMockUpSkipTextView.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

}