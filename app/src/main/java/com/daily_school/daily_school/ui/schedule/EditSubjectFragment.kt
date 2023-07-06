package com.daily_school.daily_school.ui.schedule

import FirebaseManager
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentEditSubjectBinding
import com.daily_school.daily_school.ui.AddSubjectSpinnerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditSubjectFragment : BottomSheetDialogFragment() {
    private val TAG = EditSubjectFragment::class.java.simpleName

    private lateinit var binding : FragmentEditSubjectBinding

    private lateinit var spinnerAdapterGrade : AddSubjectSpinnerAdapter
    private val listOfGrade = ArrayList<AddSubjectInfoSpinnerModel>()

    private lateinit var subjectColors: Array<String>
    private var spinnerSelectPosition = 0

    private val firebaseManager = FirebaseManager()

    private lateinit var subjectName : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_subject, container, false)

        subjectColors = resources.getStringArray(R.array.subject_color_spinner_grade)

        // 배경색 지정 셋업 함수
        setupSpinnerGrade()

        // 스피너 핸들러 셋업 함수
        setupSpinnerHandler()

        // 과목명 입력 에디터 핸들러 셋업 함수
        addSubjectEditorHandler()

        // 프래그먼트를 종료하는 함수 호출
        finishFragment()

        // 수정 과목 셋업 함수
        subjectInit()

        selectButtonClick()

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
        return getWindowHeight()// * 100 / 100
    }

    // Window 크기 조절 함수
    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    // 프래그먼트를 종료하는 함수
    private fun finishFragment(){

        binding.editSubjectIcBack.setOnClickListener {

            dismiss()

        }
    }

    // 배경색 지정 스피너 셋업 함수
    private fun setupSpinnerGrade() {
        val sGrades = resources.getStringArray(R.array.subject_color_spinner_grade)

        for (i in sGrades.indices){
            val sGrade = AddSubjectInfoSpinnerModel(sGrades[i])
            listOfGrade.add(sGrade)
        }
        spinnerAdapterGrade = AddSubjectSpinnerAdapter(requireContext(), R.layout.item_student_info_spinner, listOfGrade)
        binding.editSubjectClassSpinner.adapter = spinnerAdapterGrade
    }

    // 스피너 핸들러 셋업 함수
    private fun setupSpinnerHandler() {

        binding.editSubjectClassSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSelectPosition = position

                if (position > 0) {
                    binding.editSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_blue)

                    if(binding.editSubjectNameEdit.text.isNotEmpty()) {
                        binding.editSubjectSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                    }
                } else {
                    binding.editSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.editSubjectClassSpinner.setBackgroundResource(R.drawable.bg_spinner_gray7)
            }
        }
    }

    // 선택 완료 버튼 클릭 이벤트
    private fun selectButtonClick() {
        binding.editSubjectSelectButton.setOnClickListener {
            val subjectName = binding.editSubjectNameEdit.text.toString()
            val subjectColor = subjectColors[spinnerSelectPosition]

            Log.d(TAG, "subjectName : ${subjectName}, subjectColor : ${subjectColor}")

            if(subjectName.isNotEmpty() && subjectColor.isNotEmpty()) {
                firebaseManager.saveSubjectData(subjectName, subjectColor)
            }
        }
    }

    // 과목명 입력 에디터 핸들러 셋업 함수
    private fun addSubjectEditorHandler() {
        binding.editSubjectNameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    if(spinnerSelectPosition != 0) {
                        binding.editSubjectSelectButton.setBackgroundResource(R.drawable.radius_blue_button)
                    }

                    binding.editSubjectNameEdit.setBackgroundResource(R.drawable.bg_blue)
                }
                else {
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun subjectInit() {
        subjectName = arguments?.getString("subject").toString()
        binding.subjectText.text = subjectName
        binding.editSubjectNameEdit.text = Editable.Factory.getInstance().newEditable(subjectName)
    }
}