package com.unicompanyent.unibgame.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.DialogFragment
import com.unicompanyent.unibgame.R
import com.unicompanyent.unibgame.ui.main.game.SceneActivity

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
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // Ігнорувати клавішу "Back"
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }
                dialog.window?.decorView?.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        )
            }
        }

        return dialog
    }
}