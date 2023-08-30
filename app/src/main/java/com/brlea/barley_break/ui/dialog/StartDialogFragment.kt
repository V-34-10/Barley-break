package com.brlea.barley_break.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.main.SceneActivity

class StartDialogFragment : DialogFragment() {

    interface DialogFragmentDismissListener {
        fun onDialogDismissed()
    }

    private var dialogFragmentDismissListener: DialogFragmentDismissListener? = null

    fun setDialogFragmentDismissListener(listener: DialogFragmentDismissListener) {
        dialogFragmentDismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.app_name)
            .setMessage(R.string.startMessage)
            .setPositiveButton(R.string.startButton) { _, _ ->
                // Dismiss the dialog when the "Start" button is clicked
                dialogFragmentDismissListener?.onDialogDismissed()
                dismiss()
            }

        // Set onCancelListener to dismiss the dialog when clicked outside
        builder.setOnCancelListener {
            dismiss()
            (activity as? SceneActivity)?.initRecycler()
        }

        return builder.create()
    }
}