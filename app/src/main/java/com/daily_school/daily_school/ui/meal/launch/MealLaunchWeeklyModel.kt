package com.daily_school.daily_school.ui.meal.launch

// 이번주 급식 날짜와 안에 있는 리사이클러뷰의 데이터 모델을 저장하는 데이터 모델
data class MealLaunchWeeklyModel(
    var date: String = "",
    var innerList: MutableList<MealLaunchWeeklyEachModel>
)