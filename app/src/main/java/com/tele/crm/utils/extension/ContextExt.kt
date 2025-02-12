package com.tele.crm.utils.extension

import android.app.Activity
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun Context.getColorCompat(@ColorRes resourceId: Int) = ContextCompat.getColor(this, resourceId)

fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.showToast(msg: String) {
    return Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.makeCall(number: String) {
    if (number.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$number") }
        this.startActivity(intent)
    }
}

fun Activity.changeStatusBarColor(color: Int) {
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = ContextCompat.getColor(this.context, color)
    }
}

fun Activity.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ))
}

fun Context.openWhatsApp(number: String,text:String,notFound:()->Unit) {
    if (number.isNotEmpty()) {
        if (isWhatsAppInstalled(this)) {
            val url = "https://api.whatsapp.com/send?phone=$number&text=$text"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(browserIntent)
        } else {
            notFound.invoke()
        }
    }
}

fun isWhatsAppInstalled(context:Context): Boolean {
    return try {
        context.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        true // WhatsApp is installed
    } catch (e: PackageManager.NameNotFoundException) {
        false // WhatsApp is not installed
    }
}

fun Context.openLinkedIn(vanityName: String) {
    if (vanityName.isNotEmpty()) {
        try {
            val url = "https://www.linkedin.com/in/$vanityName"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(browserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun Context.copyText(text:String){
    val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

fun Context.getVersion(): String {

    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.getLongVersionCode().toInt()
        } else {
            packageInfo.versionCode
        }
        "$versionCode($versionName)"
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace();
        ""
    }
}

fun Context.clearAllNotifications(){
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
}


fun Activity.setStatusBar(isTransparent: Boolean, color: Int) {
    if (isTransparent) {
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    } else {
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    }

    setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
    this.window.statusBarColor = color
}

fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}
fun Context.pxToDp(px: Float): Int {
    val density: Float = resources.displayMetrics.density
    return Math.round(px / density)
}