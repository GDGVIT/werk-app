package com.dscvit.werk.ui.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.dscvit.werk.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun View.showErrorSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(Color.RED)
        .show()
}

fun View.showSuccessSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(Color.parseColor("#008000"))
        .show()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun Context.buildLoader(): androidx.appcompat.app.AlertDialog {
    val dialog = MaterialAlertDialogBuilder(this)

    dialog.setCancelable(false)
    dialog.setView(R.layout.loading_layout)

    return dialog.create()
}