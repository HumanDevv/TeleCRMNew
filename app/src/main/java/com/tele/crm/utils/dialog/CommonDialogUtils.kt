package com.tele.crm.utils.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Insets
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tele.crm.R
import com.tele.crm.databinding.AppLogoutDialogBinding
import com.tele.crm.databinding.NetworkDialogBinding
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.utils.extension.isNetworkConnected
import com.tele.crm.utils.extension.setDebouncedOnClickListener

object CommonDialogUtils {

    interface DialogClick {
        fun onClick()
        fun onCancel()
    }




    fun showAppLogOutDialog(
        context: Context,
        title: String,
        msg: String,
        okText: String,
        onClick: () -> Unit
    ) {
        val layoutInflater = LayoutInflater.from(context)
        val binding = AppLogoutDialogBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        with(binding) {
            tvTitle.text = title
            tvMessage.text = msg
            tvOk.text = okText

            tvOk.setDebouncedOnClickListener {
                onClick()
                alertDialog.dismiss()
            }

            tvCancel.setDebouncedOnClickListener {
                alertDialog.dismiss()
            }


        }

        alertDialog.apply {
            window?.attributes?.windowAnimations = R.style.DialogAnimationTheme
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setLayout(
                (getDeviceWidth() / 100) * 90,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            // Ensure the dialog appears in the center
            window?.setGravity(Gravity.CENTER)

            setCanceledOnTouchOutside(true)
            setCancelable(true)
            show()
        }
    }


    fun networkDialog(context: Activity): Dialog {
        val binding: NetworkDialogBinding =
            NetworkDialogBinding.inflate(LayoutInflater.from(context), null, false)
        val dialog = Dialog(context, R.style.DialogAnimationTheme)
        binding.btnRetry.setDebouncedOnClickListener {
            if (context.isNetworkConnected()) {
                dialog.dismiss()
            } else {
                Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        return dialog
    }


    private fun getDeviceWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

    @Suppress("unused")
    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Show the soft input.
     *
     * @param activity The activity.
     */
    @Suppress("unused", "DEPRECATION")
    fun showSoftInput(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    @Suppress("unused")
    fun hideSoftInput(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Hide the soft input.
     */
    @Suppress("unused")
    fun hideSoftInput(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    @Suppress("DEPRECATION")
    fun getScreenWidth(mContext: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = mContext.windowManager.currentWindowMetrics
            val insets: Insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            mContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

}