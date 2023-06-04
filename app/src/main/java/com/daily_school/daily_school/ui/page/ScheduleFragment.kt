package com.daily_school.daily_school.ui.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentPlanBinding
import com.daily_school.daily_school.databinding.FragmentScheduleBinding
import com.daily_school.daily_school.ui.plan.AddTodoActivity
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity


class ScheduleFragment : Fragment() {
    private lateinit var binding : FragmentScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        // 과목 추가 버튼 클릭 이벤트
        binding.scheduleAddArea.setOnClickListener {
            var intent = Intent(context, AddSubjectActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}