package com.daily_school.daily_school.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.daily_school.daily_school.R

import com.daily_school.daily_school.databinding.AccountDialogBinding
import com.daily_school.daily_school.ui.LoginActivity
import com.kakao.sdk.user.UserApiClient

class AccountDialog : DialogFragment() {
    private lateinit var binding : AccountDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.account_dialog, container, false)

        binding.accountCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.accountDeleteBtn.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("AccountDialog", "회원 탈퇴 실패")
                }else {
                    Log.i("AccountDialog", "회원 탈퇴 성공")
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }
            }
        }

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

        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.3).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}