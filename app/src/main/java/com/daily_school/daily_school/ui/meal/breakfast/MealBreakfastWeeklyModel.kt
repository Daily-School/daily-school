package com.daily_school.daily_school.ui.meal.breakfast

data class MealBreakfastWeeklyModel (
    var breakfastDate : String = "",
    var innerList : MutableList<MealBreakfastWeeklyEachModel>
)