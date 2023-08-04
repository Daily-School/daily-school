package com.daily_school.daily_school.ui.home

import FirebaseManager
import android.app.Activity
import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentHomeCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.user.UserApiClient
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HomeCalendarFragment(text: String, id: Int) : BottomSheetDialogFragment(),
    HomeOnItemListener {

    private var TAG = HomeCalendarFragment::class.java.simpleName

    private lateinit var binding : FragmentHomeCalendarBinding

    private var prePosition : Int = -1

    private var selectedDate = LocalDate.now()

    private var currentDate = LocalDate.now()

    private var firebaseManager = FirebaseManager()

    private var text : String? = null
    private var id : Int? = null

    init {
        this.text = text
        this.id = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_calendar, container, false)

        binding.homeCalendarSelectedTextView.text = text
        binding.homeCalendarSelectedTextView.setTypeface(binding.homeCalendarSelectedTextView.typeface, Typeface.BOLD)

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

    override fun onItemClick(dayText: String, position: Int) {

        if(prePosition == position){
            prePosition = -1
            var yearMonthDay = yearMonthFromDate(selectedDate) + dayText

            Log.d(TAG, yearMonthDay)

            if (id == 0){

                UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
                    if (error != null){
                        Log.e(TAG, "토큰 정보 보기 실패", error)
                    }
                    else if (tokenInfo != null){

                        firebaseManager.saveMidDDayDateInfoData(tokenInfo.id.toString(), yearMonthDay)

                    }
                }
                setFragmentResult("MidDateKey", bundleOf("MidDateKey1" to yearMonthDay))
            }
            else if (id == 1){
                UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
                    if (error != null){
                        Log.e(TAG, "토큰 정보 보기 실패", error)
                    }
                    else if (tokenInfo != null){

                        firebaseManager.saveFinalDDayDateInfoData(tokenInfo.id.toString(), yearMonthDay)

                    }
                }
                setFragmentResult("FinalDateKey", bundleOf("FinalDateKey1" to yearMonthDay))
            }

//            val transaction = fragmentManager?.beginTransaction()
//            transaction?.replace(R.id.homeLayout, HomeFragment())
//            transaction?.commit()

            dismiss()
        }
        else{
            var yearMonthDay = monthYearFromDate(selectedDate) + " " + dayText + "일"
            binding.homeCalendarDateTextView.text = yearMonthDay
            prePosition = position
        }
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

    // 년월 포맷 형식을 리턴하는 함수
    private fun yearMonthFromDate(date : LocalDate): String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
        return date.format(formatter)
    }

    // 달력 adapter 연결하는 함수
    private fun setMonthYear(){
        binding.homeCalendarYearTextView.text = monthYearFromDate(selectedDate)

        val dayList = calendarArray(selectedDate)
        val homeCalendarRvAdapter = HomeCalendarRvAdapter(requireContext(), dayList, this)

        val homeCalendarRv = binding.homeCalendarRv

        homeCalendarRv.adapter = homeCalendarRvAdapter

        homeCalendarRv.layoutManager = GridLayoutManager(requireContext(), 7)
    }

    // 저번 달로 돌아가는 함수
    private fun backToMonth(){

        binding.homeIcBack.setOnClickListener {

            selectedDate = selectedDate.minusMonths(1)
            setMonthYear()

        }

    }

    // 다음 달로 넘어가는 함수
    private fun frontToMonth(){

        binding.homeIcFront.setOnClickListener {

            selectedDate = selectedDate.plusMonths(1)
            setMonthYear()

        }

    }

    // 프래그먼트를 종료하는 함수
    private fun finishFragment(){

        binding.homeCalendarIcBack.setOnClickListener {

            dismiss()

        }
    }

    // 날짜를 불러오는 함수
    private fun loadCurrentDate(date : LocalDate) : String? {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일").withLocale(
            Locale.forLanguageTag("ko"))
        return date.format(formatter)
    }

    // 텍스트뷰에 날짜를 연결하는 함수
    private fun setCurrentDate(){
        binding.homeCalendarDateTextView.text = loadCurrentDate(currentDate)
    }

    // array에 실제 날짜를 연결하는 함수
    private fun calendarArray(date : LocalDate) : ArrayList<String> {
        val dayList = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val lastDay = yearMonth.lengthOfMonth()
        val firstDay : LocalDate = selectedDate.withDayOfMonth(1)

        val dayOfWeek= firstDay.dayOfWeek.value

        for (i : Int in 1..41){
            if (i <= dayOfWeek || i > (lastDay + dayOfWeek)){
                dayList.add("")
            }
            else{
                dayList.add((i-dayOfWeek).toString())
            }
        }

        return dayList

    }

}