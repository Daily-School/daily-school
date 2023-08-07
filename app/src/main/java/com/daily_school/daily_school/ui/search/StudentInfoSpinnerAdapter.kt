package com.daily_school.daily_school.ui.search

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.daily_school.daily_school.databinding.ItemStudentInfoSpinnerBinding

class StudentInfoSpinnerAdapter(context: Context, @LayoutRes private val resId: Int, private val values: MutableList<StudentInfoSpinnerModel>)
    : ArrayAdapter<StudentInfoSpinnerModel>(context, resId, values)
{
    // 배열의 크기 불러오는 함수
    override fun getCount(): Int {
        return super.getCount() - 1
    }

    // 배열의 값 불러오는 함수
    override fun getItem(position: Int): StudentInfoSpinnerModel? {
        return values[position]
    }

    // view를 보여주는 함수
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemStudentInfoSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]

        if (position == count) {
            binding.spinnerTextView.text = model.info
            binding.spinnerTextView.setGravity(Gravity.LEFT)
            binding.spinnerTextView.hint = getItem(count).toString()
        }

        binding.spinnerTextView.text = model.info
        binding.spinnerTextView.setGravity(Gravity.LEFT)

        return binding.root
    }

    // 스피너 DropDown이 펼쳐졌을 때 보여지는 함수
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemStudentInfoSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.spinnerTextView.text = model.info
        } catch (e : Exception){
            e.printStackTrace()
        }
        return binding.root
    }
}