package com.brlea.barley_break.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.brlea.barley_break.R
import com.brlea.barley_break.ui.main.SceneActivity

class VictoryDialogFragment : DialogFragment() {

    interface DialogFragmentDismissListener {
        fun onRestartClicked()
    }

    private var dialogFragmentDismissListener: DialogFragmentDismissListener? = null

    fun setDialogFragmentDismissListener(listener: SceneActivity) {
        dialogFragmentDismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.titleVictory)
            .setMessage(R.string.victoryMessage)
            .setPositiveButton(R.string.restartButton) { _, _ ->
                // Notify the listener that the "Restart" button is clicked
                dialogFragmentDismissListener?.onRestartClicked()
                dismiss()
            }

        // Set onCancelListener to dismiss the dialog when clicked outside
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setOnShowListener {
            dialog.window?.decorView?.apply {
                setOnTouchListener { _, _ -> true }
                setOnClickListener(null)
            }
        }

        return dialog
    }
}