package com.daily_school.daily_school.ui.plan

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.daily_school.daily_school.R

class TodoCompleteDialog : DialogFragment() {
    private var todoCompleteListener: TodoCompleteDialog? = null

    interface TodoCompleteDialog {
        fun onNoButtonClicked()
        fun onYesButtonClicked()
    }

    fun setCompleteDialogListener(listener: TodoCompleteDialog) {
        todoCompleteListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.todo_complete_dialog, null)

        builder.setView(view)

        val dialog = builder.create()

        view.findViewById<View>(R.id.noButtonArea)?.setOnClickListener {
            todoCompleteListener?.onNoButtonClicked()
            dialog.dismiss()
        }

        view.findViewById<View>(R.id.yesButtonArea)?.setOnClickListener {
            todoCompleteListener?.onYesButtonClicked()
            dialog.dismiss()
        }

        return dialog
    }
}