package com.daily_school.daily_school.ui.meal.launch

data class MealLaunchWeeklyModel (
    var date : String = "",
    var innerList: MutableList<MealLaunchWeeklyEachModel>
)