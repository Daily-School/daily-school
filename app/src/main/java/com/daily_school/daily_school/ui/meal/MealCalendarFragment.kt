package com.daily_school.daily_school.ui.meal

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MealCalendarFragment : BottomSheetDialogFragment(){

    private var selectedDate = LocalDate.now()

    private var currentDate = LocalDate.now()

    private lateinit var binding : FragmentMealCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_calendar, container, false)

        // 달력 adapter 연결하는 함수 호출
        setMonthYear()

        // 텍스트뷰에 날짜를 연결하는 함수 호출
        setCurrentDate()

        // 저번 달로 돌아가는 함수 호출
        backToMonth()

        // 다음 달로 넘어가는 함수 호출
        frontToMonth()

        // 프래그먼트를 종료하는 함수 호출
        finishFragment()

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            // 다이얼로그 크기 설정 (인자값 : DialogInterface)
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    /** setupRatio()
     * bottomSheet
     *      - 전달받은 DialogInterface를 통해 View를 참조하도록 한다.
     *      - 이때 id 값에는 내가 만든 View가 들어가는게 아님을 주의하자.
     * layoutParams
     *      - View를 감싸고 있는 Parents에게 어떻게 레이아웃 할 것인지 설정하는데 사용함.
     * behavior
     *      - 생성한 View를 통해 BottomSheetBehavior를 생성한다.
     *      - 역할 : BottomSheetBehavior로 상태값을 '확장형'으로 설정해준다.
     * */
    // BottomSheetDialog Interface를 view에 전달하는 함수
    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    // BottomSheetDialog 높이 조절 함수
    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 75 / 100
    }

    // Window 크기 조절 함수
    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    // 년월 포맷 형식을 리턴하는 함수
    private fun monthYearFromDate(date : LocalDate): String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        return date.format(formatter)
    }

    // 달력 adapter 연결하는 함수
    private fun setMonthYear(){
        binding.mealCalendarYearTextView.text = monthYearFromDate(selectedDate)

        val dayList = calendarArray(selectedDate)
        val mealCalendarRvAdapter = MealCalendarRvAdapter(requireContext(), dayList)

        val mealCalendarRv = binding.mealCalendarRv

        mealCalendarRv.adapter = mealCalendarRvAdapter

        mealCalendarRv.layoutManager = GridLayoutManager(requireContext(), 7)
    }

    // 저번 달로 돌아가는 함수
    private fun backToMonth(){

        binding.mealIcBack.setOnClickListener {

            selectedDate = selectedDate.minusMonths(1)
            setMonthYear()

        }

    }

    // 다음 달로 넘어가는 함수
    private fun frontToMonth(){

        binding.mealIcFront.setOnClickListener {

            selectedDate = selectedDate.plusMonths(1)
            setMonthYear()

        }

    }

    // 프래그먼트를 종료하는 함수
    private fun finishFragment(){

        binding.mealCalendarIcBack.setOnClickListener {

            dismiss()

        }
    }

    // 날짜를 불러오는 함수
    private fun loadCurrentDate(date : LocalDate) : String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)").withLocale(
            Locale.forLanguageTag("ko"))
        return date.format(formatter)
    }

    // 텍스트뷰에 날짜를 연결하는 함수
    private fun setCurrentDate(){
        binding.mealCalendarDateTextView.text = loadCurrentDate(currentDate)
    }

    // array에 실제 날짜를 연결하는 함수
    private fun calendarArray(date : LocalDate) : ArrayList<String> {
        val dayList = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val lastDay : Int = yearMonth.lengthOfMonth()
        val firstDay : LocalDate = selectedDate.withDayOfMonth(1)

        val dayOfWeek : Int = firstDay.dayOfWeek.value

        for (i : Int in 1..42){
            if (i <= dayOfWeek || i > lastDay + dayOfWeek){
                dayList.add("")
            }
            else{
                dayList.add((i-dayOfWeek).toString())
            }
        }

        return dayList

    }

}