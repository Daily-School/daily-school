package com.daily_school.daily_school.ui.search

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentSchoolNameBinding
import com.daily_school.daily_school.databinding.SchoolNameRvItemBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.utils.NeisRef
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SchoolNameFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentSchoolNameBinding

    private val schoolInfoAdapter by lazy{ SchoolInfoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_name, container, false)

        binding.schoolNameInfoRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = schoolInfoAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        searchSchoolInfo()

        closeFragment()

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
        return getWindowHeight() * 90 / 100
    }

    // Window 크기 조절 함수
    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    // 학교 검색을 했을 때 해당 학교를 검색하는 함수
    private fun searchSchoolInfo(){

        // 검색 아이콘을 눌렀을 때
        binding.schoolNameIcSearch.setOnClickListener {
            val service = RetrofitApi.schoolInfoService

            // EditText 커서 없애줌
            binding.schoolNameSearchEditText.clearFocus()

            // neis 학교정보 api를 1페이지에서 12페이지까지 불러와 해당 학교가 있는지 찾음
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 1..12){
                    val response = service.getDataCoroutine(NeisRef.api_key, "json", i, 1000, binding.schoolNameSearchEditText.text.toString())

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val result = response.body()?.schoolInfo?.get(1)?.row
                            result?.let {
                                schoolInfoAdapter.submitList(it)
                            }
                        } else {
                            Log.e("SchoolNameFragment", response.code().toString())
                        }
                    }
                }

            }

            // 원하는 학교를 선택하여 그 학교의 정보를 EditText안에 넣어줌
            schoolInfoAdapter.listItemClickFunc(object : SchoolInfoAdapter.OnItemClickListener{
                val sNm = binding.schoolNameSearchEditText
                override fun setOnItemClickListener(
                    itemData: String,
                    cityCode: String,
                    schoolCode: String,
                    binding: SchoolNameRvItemBinding
                ) {
                    sNm.setText(itemData)
                    val mActivity = activity as SchoolInfoActivity
                    mActivity.receiveData(sNm.text.toString())
                    mActivity.receiveSchoolData(cityCode, schoolCode)
                    dismiss()
                }
            })

        }

    }

    private fun closeFragment(){
        binding.schoolNameIcBack.setOnClickListener {
            dismiss()
        }
    }

}