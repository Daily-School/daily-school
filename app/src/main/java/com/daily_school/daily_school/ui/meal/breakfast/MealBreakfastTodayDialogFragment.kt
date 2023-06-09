package com.daily_school.daily_school.ui.meal.breakfast

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealBreakfastTodayDialogBinding

class MealBreakfastTodayDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentMealBreakfastTodayDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_breakfast_today_dialog, container, false)

        todayMeal()

        fragmentFinish()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params : ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * 0.7).toInt()
        params?.height = (deviceHeight * 0.6).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun todayMeal(){

        val todayItems = ArrayList<MealBreakfastTodayModel>()

        todayItems.add(MealBreakfastTodayModel("토마토 스파게티"))
        todayItems.add(MealBreakfastTodayModel("피클"))
        todayItems.add(MealBreakfastTodayModel("배추김치"))
        todayItems.add(MealBreakfastTodayModel("피크닉"))
        todayItems.add(MealBreakfastTodayModel("미트볼"))
        todayItems.add(MealBreakfastTodayModel("새우튀김"))

        val todayBreakfastDialogRvAdapter = MealBreakfastTodayDialogRvAdapter(todayItems)

        val todayBreakfastDialogRv = binding.mealBreakfastTodayDialogRv

        todayBreakfastDialogRv.adapter = todayBreakfastDialogRvAdapter

        todayBreakfastDialogRv.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    private fun fragmentFinish(){
        binding.mealBreakfastIcFinish.setOnClickListener {
            dismiss()
        }
    }

}