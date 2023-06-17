package com.daily_school.daily_school.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.daily_school.daily_school.R
import java.util.*

open class CalendarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.calendar_component, this)

        val dates = getMonthDates()
        val adapter = CalendarAdapter(context, dates)
        val gridView: GridView = findViewById(R.id.gridCalendar)
        gridView.adapter = adapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val selectedTextView = view as TextView
            val selectedDateText = selectedTextView.text.toString()

            if (selectedDateText.isNotEmpty()) {
                adapter.selectedDate = position
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getMonthDates(): ArrayList<String> {
        val dates = ArrayList<String>()

        // 현재 월과 년도 가져오기
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        // 현재 월의 첫 번째 일자로 설정
        calendar.set(currentYear, currentMonth, 1)

        // 현재 월의 첫 번째 일자의 요일 가져오기 (일: 1, 월: 2, ..., 토: 7)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // 현재 월의 마지막 일자 가져오기
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // 첫 번째 일자 이전의 빈 칸 채우기
        for (i in 1 until firstDayOfWeek) {
            dates.add("")
        }

        // 현재 월의 일자 채우기
        for (i in 1..lastDayOfMonth) {
            val date = i.toString()

            val textView = TextView(context)
            textView.text = date
            dates.add(date)
        }

        return dates
    }
}

class CalendarAdapter(context: Context, dates: List<String>) : BaseAdapter() {

    private val mContext: Context = context
    private val mDates: List<String> = dates
    internal var selectedDate: Int = -1
    private val setSize: (Int) -> Int = { dp ->
        (dp * mContext.resources.displayMetrics.density).toInt()
    }

    override fun getCount(): Int {
        return mDates.size
    }

    override fun getItem(position: Int): Any {
        return mDates[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val date = mDates[position]

        val textView = if (convertView == null) {
            TextView(mContext).apply {
                layoutParams = ViewGroup.LayoutParams(setSize(60), setSize(60))
                gravity = Gravity.CENTER
            }
        } else {
            convertView as TextView
        }

        textView.text = date
        textView.textSize = 16f
        textView.setTypeface(null, Typeface.BOLD)

        // 토요일
        if (position % 7 == 6) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.saturday_color))
        }
        // 일요일
        else if (position % 7 == 0) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.sunday_color))
        } else {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        }

        // 빈 일자인 경우 클릭 불가능하도록 처리
        textView.isEnabled = !date.isEmpty()

        // 일자가 선택되었을 때
        if (position == selectedDate) {
            textView.setBackgroundResource(R.drawable.button_circle)
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        }
        else {
            textView.background = null
        }

        return textView
    }
}