package pl.piterowsky.runinga.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlin.text.StringBuilder

class InputTimeWatcher(private val context: Context, private val input: EditText) : TextWatcher{

    private var deleting = false

    override fun afterTextChanged(s: Editable?) {
        input.removeTextChangedListener(this)

        if(s != null) {
            formatInputText(s)
            hideKeyboardWhenMaxLength(s)
        }

        input.addTextChangedListener(this)
    }

    private fun hideKeyboardWhenMaxLength(s: Editable) {
        val maxInputLength = 5
        if (s.length == maxInputLength) {
            hideKeyboard();
        }
    }

    private fun formatInputText(s: Editable) {
        val delimiter = ":"
        val withoutDelimiter: String = StringBuilder(s).replace(Regex(delimiter), "")
        if (s.length > 1 && !deleting) {
            val withDelimiter = StringBuilder(withoutDelimiter).insert(2, delimiter)
            input.setText(withDelimiter.toString())
            input.setSelection(withDelimiter.length)
        }
    }

    private fun hideKeyboard() {
        val view: View? = (context as Activity).currentFocus
        if (view != null) {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        deleting = (count == 1)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Not implemented
    }
}
