package com.brlea.barley_break.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.brlea.barley_break.R

class InfoDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.app_name)
            .setMessage(R.string.infoMessage)
            .setPositiveButton(R.string.okButton) { _, _ ->
                // Dismiss the dialog when the "ok" button is clicked
                dismiss()
            }
        return builder.create()
    }
}