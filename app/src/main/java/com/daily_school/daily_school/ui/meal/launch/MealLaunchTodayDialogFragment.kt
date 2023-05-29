package com.daily_school.daily_school.ui.meal.launch

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
import com.daily_school.daily_school.databinding.FragmentMealLaunchTodayDialogBinding
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayDialogRvAdapter
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayModel

class MealLaunchTodayDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentMealLaunchTodayDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_launch_today_dialog, container, false)

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

        val todayItems = ArrayList<MealLaunchTodayModel>()

        todayItems.add(MealLaunchTodayModel("치킨마요 덮밥"))
        todayItems.add(MealLaunchTodayModel("단무지"))
        todayItems.add(MealLaunchTodayModel("탕수육"))
        todayItems.add(MealLaunchTodayModel("쿨피스"))
        todayItems.add(MealLaunchTodayModel("소고기 무국"))

        val todayLaunchDialogRvAdapter = MealLaunchTodayDialogRvAdapter(todayItems)

        val todayLaunchDialogRv = binding.mealLaunchTodayDialogRv

        todayLaunchDialogRv.adapter = todayLaunchDialogRvAdapter

        todayLaunchDialogRv.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    private fun fragmentFinish(){
        binding.mealLaunchIcFinish.setOnClickListener {
            dismiss()
        }
    }

}