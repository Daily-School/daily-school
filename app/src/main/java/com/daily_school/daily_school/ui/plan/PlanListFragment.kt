package com.daily_school.daily_school.ui.plan

import FirebaseManager
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentPlanListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class PlanListFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentPlanListBinding

    private var currentDate: String? = ""
    private var todoListArray: List<Map<String, Any>> = mutableListOf()

    private val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_list, container, false)

        // 프래그먼트를 종료하는 함수 호출
        finishFragment()

        // 데이터 저장 함수 호출
        todoListDataInit()

        // 현재 날짜 요일 셋업 함수 호출
        currentDateInit()

        // 할 일 목록을 보여주는 함수 호출
        showTodoList()

        // 다시 보지 않기 설정 함수 호출
        neverShowGuideText()

        // 다시 보기 상태 가져오는 함수 호출
        getNeverShowGuideText()

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    // BottomSheetDialog Interface를 view에 전달하는 함수
    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    // BottomSheetDialog 높이 조절 함수
    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    // Window 크기 조절 함수
    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    // 프래그먼트를 종료하는 함수
    private fun finishFragment(){
        binding.planListIcBack.setOnClickListener {
            dismiss()
        }
    }

    // 데이터 저장 함수
    private fun todoListDataInit() {
        val gson = Gson()
        val filteredTodoListJson = arguments?.getString("todoInfo")
        currentDate = arguments?.getString("todoDate")
        todoListArray = gson.fromJson(filteredTodoListJson, object : TypeToken<List<Map<String, Any>>>() {}.type)
    }

    // 현재 날짜 요일 셋업 함수
    private fun currentDateInit() {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-d", Locale.getDefault())
        val date = inputDateFormat.parse(currentDate)

        val outputDateFormat = SimpleDateFormat("yyyy년 M월 d일 (E)", Locale.getDefault())
        binding.planListDateTextView.text = outputDateFormat.format(date)
    }

    // 할 일 목록을 보여주는 함수
    private fun showTodoList() {
        val planTodoLayout = binding.planTodoLayout
        val inflater = LayoutInflater.from(requireContext())

        planTodoLayout.removeAllViews()

        for (todoItem in todoListArray) {
            val planTodoItem = inflater.inflate(R.layout.plan_todo_item, planTodoLayout, false)

            val planTodoColorImageView = planTodoItem.findViewById<ImageView>(R.id.planTodoColorImageView)
            val planTodoTextView = planTodoItem.findViewById<TextView>(R.id.planTodoTextView)

            val todoName = todoItem["todoName"] as? String
            val todoColor = todoItem["todoColor"] as? String
            val todoComplete = todoItem["todoComplete"] as? Boolean

            todoColor?.let {
                val colorResourceId = getTodoColorResourceId(it)
                planTodoColorImageView.setBackgroundResource(colorResourceId)
            }

            if(todoComplete == true) {
                val textColorResourceId = R.color.gray_6
                val textColor = ContextCompat.getColor(requireContext(), textColorResourceId)

                planTodoTextView.paintFlags = planTodoTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                planTodoTextView.setTextColor(textColor)
            }

            planTodoItem.setOnClickListener {
                // 완료 다이얼로그 호출
                showTodoCompleteDialog(todoName.toString())
            }

            planTodoTextView.text = todoName
            planTodoLayout.addView(planTodoItem)
        }
    }

    // 달성 완료 다이얼로그 함수
    private fun showTodoCompleteDialog(todoName: String) {
        val completeDialog = TodoCompleteDialog()
        completeDialog.setCompleteDialogListener(object : TodoCompleteDialog.TodoCompleteDialog {
            // 아니요 버튼 클릭 시 동작
            override fun onNoButtonClicked() {
            }

            // 예 버튼 클릭 시 동작
            override fun onYesButtonClicked() {
                dismiss()
                firebaseManager.updateTodoComplete(todoName)
            }
        })
        completeDialog.show(requireActivity().supportFragmentManager, completeDialog.tag)
    }

    // 할 일 목록 색상 지정 함수
    private fun getTodoColorResourceId(todoColor: String): Int {
        return when (todoColor) {
            TodoColor.RED.name -> R.color.red_color
            TodoColor.BLUE.name -> R.color.main_color
            TodoColor.YELLOW.name -> R.color.yellow_color
            TodoColor.ORANGE.name -> R.color.orange_color
            TodoColor.GREEN.name -> R.color.light_green_color
            TodoColor.PURPLE.name -> R.color.pink_color
            else -> R.color.main_color
        }
    }

    // 다시 보지 않기 설정 함수
    private fun neverShowGuideText() {
        binding.planTodoNeverShowTextView.setOnClickListener {
            binding.planTodoGuideArea.visibility = View.GONE

            setNeverShowAgainStatus(true)
        }
    }

    // 다시 보기 상태 가져오는 함수
    private fun getNeverShowGuideText() {
        val neverShowAgainStatus = getNeverShowAgainStatus()
        if (neverShowAgainStatus) {
            binding.planTodoGuideArea.visibility = View.GONE
        }
        else {
            binding.planTodoGuideArea.visibility = View.VISIBLE
        }
    }

    // SharedPreferences Set 함수
    private fun setNeverShowAgainStatus(status: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences("todoGuide", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("show", status)
        editor.apply()
    }

    // SharedPreferences Get 함수
    private fun getNeverShowAgainStatus(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("todoGuide", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("show", false)
    }
}