import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.daily_school.daily_school.R

class DeleteSubjectDialog : DialogFragment() {

    private var deleteSubjectListener: DeleteSubjectListener? = null

    interface DeleteSubjectListener {
        fun onNoButtonClicked()
        fun onYesButtonClicked()
    }

    fun setDeleteSubjectListener(listener: DeleteSubjectListener) {
        deleteSubjectListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.subject_delete_dialog, null)

        builder.setView(view)

        val dialog = builder.create()

        view.findViewById<View>(R.id.noButtonArea)?.setOnClickListener {
            deleteSubjectListener?.onNoButtonClicked()
            dialog.dismiss()
        }

        view.findViewById<View>(R.id.yesButtonArea)?.setOnClickListener {
            deleteSubjectListener?.onYesButtonClicked()
            dialog.dismiss()
        }

        return dialog
    }
}