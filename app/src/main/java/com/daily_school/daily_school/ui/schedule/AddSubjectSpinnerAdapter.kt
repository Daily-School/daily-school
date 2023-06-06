package com.daily_school.daily_school.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.ItemAddSubjectSpinnerBinding
import com.daily_school.daily_school.databinding.ItemStudentInfoSpinnerBinding
import com.daily_school.daily_school.ui.schedule.AddSubjectInfoSpinnerModel

class AddSubjectSpinnerAdapter(context: Context, @LayoutRes private val resId: Int, private val values: MutableList<AddSubjectInfoSpinnerModel>)
    : ArrayAdapter<AddSubjectInfoSpinnerModel>(context, resId, values)
{
    // 배열의 크기 불러오는 함수
    override fun getCount(): Int {
        return values.size
    }

    // 배열의 값 불러오는 함수
    override fun getItem(position: Int): AddSubjectInfoSpinnerModel? {
        return values[position]
    }

    // view를 보여주는 함수
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemAddSubjectSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.spinnerTextView.text = model.info
            binding.spinnerTextView.gravity = Gravity.LEFT
            val params = binding.spinnerTextView.layoutParams as LinearLayout.LayoutParams
            params.marginStart = if (position == 0) 40 else 0
            binding.spinnerTextView.layoutParams = params
            binding.upArrowImageView.visibility = View.GONE
            binding.colorImageView.visibility = if (position == 0) View.GONE else View.VISIBLE
            setCircleColor(binding.colorImageView, position)
        } catch (e : Exception){
            e.printStackTrace()
        }
        return binding.root
    }

    // 스피너 DropDown이 펼쳐졌을 때 보여지는 함수
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemAddSubjectSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.spinnerTextView.text = model.info
            val params = binding.spinnerTextView.layoutParams as LinearLayout.LayoutParams
            params.marginStart = if (position == 0) 40 else 0
            binding.spinnerTextView.layoutParams = params
            binding.upArrowImageView.visibility = if (position == 0) View.VISIBLE else View.GONE
            binding.colorImageView.visibility = if (position == 0) View.GONE else View.VISIBLE
            setCircleColor(binding.colorImageView, position)
        } catch (e : Exception){
            e.printStackTrace()
        }
        return binding.root
    }

    // 과목 선택 색상 설정 함수
    private fun setCircleColor(colorImageView: View, position: Int) {
        val colors = arrayOf(
            ContextCompat.getColor(context, R.color.subject_default_color),
            ContextCompat.getColor(context, R.color.subject_default_color),
            ContextCompat.getColor(context, R.color.subject_light_blue_color),
            ContextCompat.getColor(context, R.color.subject_light_pink_color),
            ContextCompat.getColor(context, R.color.subject_light_lemon_color),
            ContextCompat.getColor(context, R.color.subject_light_purple_color),
            ContextCompat.getColor(context, R.color.subject_light_salmon_color),
            ContextCompat.getColor(context, R.color.subject_light_green_color)
        )
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setColor(colors[position % colors.size])
        colorImageView.background = drawable
    }
}