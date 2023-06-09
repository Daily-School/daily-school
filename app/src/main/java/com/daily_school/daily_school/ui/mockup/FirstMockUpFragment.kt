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
import com.daily_school.daily_school.databinding.FragmentFirstMockUpBinding
import com.daily_school.daily_school.ui.LoginActivity

class FirstMockUpFragment : Fragment() {

    private lateinit var binding : FragmentFirstMockUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_mock_up, container, false)

        // 프래그먼트 화면 이동 함수 호출
        moveFragment()

        // 로그인 액티비티로 이동하는 함수 호출
        fragmentToLogin()

        return binding.root
    }

    // 프래그먼트 화면 이동 함수
    private fun moveFragment(){

        binding.firstMockUpIcSecondRolling.setOnClickListener {
            it.findNavController().navigate(R.id.action_firstMockUpFragment_to_secondMockUpFragment)
        }

        binding.firstMockUpIcThirdRolling.setOnClickListener {
            it.findNavController().navigate(R.id.action_firstMockUpFragment_to_thirdMockUpFragment)
        }

        binding.firstMockUpBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_firstMockUpFragment_to_secondMockUpFragment)
        }

    }

    // 로그인 액티비티로 이동하는 함수
    private fun fragmentToLogin(){
        binding.firstMockUpSkipTextView.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

}