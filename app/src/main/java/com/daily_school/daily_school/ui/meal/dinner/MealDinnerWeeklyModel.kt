package com.daily_school.daily_school.ui.meal.dinner

data class MealDinnerWeeklyModel (
    var dinnerDate : String = "",
    var innerList : MutableList<MealDinnerWeeklyEachModel>
)