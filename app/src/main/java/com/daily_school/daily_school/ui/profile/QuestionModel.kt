package com.daily_school.daily_school.ui.profile

//자주 묻는 질문 내용 데이터 모델
data class QuestionModel (
    val question : String,
    val innerModel : MutableList<QuestionDetailModel>
)