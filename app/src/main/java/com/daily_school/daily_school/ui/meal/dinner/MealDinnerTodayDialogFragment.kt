package com.daily_school.daily_school.ui.meal.dinner

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
import com.daily_school.daily_school.databinding.FragmentMealDinnerTodayDialogBinding

class MealDinnerTodayDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentMealDinnerTodayDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_dinner_today_dialog, container, false)

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

        val todayItems = ArrayList<MealDinnerTodayModel>()

        todayItems.add(MealDinnerTodayModel("날치알김치덮밥"))
        todayItems.add(MealDinnerTodayModel("유부장국"))
        todayItems.add(MealDinnerTodayModel("버섯돈육불고기"))
        todayItems.add(MealDinnerTodayModel("치커리사과무침"))
        todayItems.add(MealDinnerTodayModel("깍두기"))
        todayItems.add(MealDinnerTodayModel("메론"))

        val todayDinnerDialogRvAdapter = MealDinnerTodayDialogRvAdapter(todayItems)

        val todayDinnerDialogRv = binding.mealDinnerTodayDialogRv

        todayDinnerDialogRv.adapter = todayDinnerDialogRvAdapter

        todayDinnerDialogRv.layoutManager = GridLayoutManager(requireContext(), 1)
    }

    private fun fragmentFinish(){
        binding.mealDinnerIcFinish.setOnClickListener {
            dismiss()
        }
    }

}