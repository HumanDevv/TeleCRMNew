package com.tele.crm.utils.extension

import android.content.ContentValues.TAG
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope

import com.tele.crm.MainActivity

val Fragment.fragmentScope: LifecycleCoroutineScope
    get() = this.viewLifecycleOwner.lifecycleScope

var toast:Toast?=null
fun Fragment.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    toast=Toast.makeText(context, msg, length)
    toast?.show()
}
fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showProgress() {
    if (activity is MainActivity) {
        (activity as
                MainActivity).showProgress()
    }
}

fun Fragment.hideProgress() {
    if (activity is MainActivity) {
        (activity as MainActivity).hideProgress()
    }
}

fun Fragment.changeStatusBarColor(color: Int) {
    requireActivity().window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = ContextCompat.getColor(this.context, color)
    }
}

