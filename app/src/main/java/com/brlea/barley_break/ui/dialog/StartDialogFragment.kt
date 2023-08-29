package com.brlea.barley_break.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.brlea.barley_break.R

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
                dismiss()
                dialogFragmentDismissListener?.onDialogDismissed()
            }
        return builder.create()
    }
}