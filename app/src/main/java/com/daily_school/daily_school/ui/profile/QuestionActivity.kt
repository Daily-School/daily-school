package com.daily_school.daily_school.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question)

        // 자주 묻는 질문 내용을 보여주는 함수 호출
        showQuestionView()

        // 프래그먼트 화면으로 돌아가는 함수 호출
        backToProfile()

    }

    // 자주 묻는 질문 내용을 보여주는 함수
    private fun showQuestionView(){

        val items = mutableListOf(
            QuestionModel(getString(R.string.question_rv_1), mutableListOf(
                QuestionDetailModel(getString(R.string.question_detail_1))
            )
            ),
            QuestionModel(getString(R.string.question_rv_2), mutableListOf(
                QuestionDetailModel(getString(R.string.question_detail_2))
            )),
            QuestionModel(getString(R.string.question_rv_3), mutableListOf(
                QuestionDetailModel(getString(R.string.question_detail_3))
            )),
        )

        binding.questionEachRv.adapter = QuestionRvAdapter(this, items)
        binding.questionEachRv.layoutManager = GridLayoutManager(this, 1)
    }

    // 프래그먼트 화면으로 돌아가는 함수
    private fun backToProfile(){

        binding.questionIcToProfile.setOnClickListener {
            finish()
        }

    }
}