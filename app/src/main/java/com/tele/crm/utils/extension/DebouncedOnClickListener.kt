package com.tele.crm.utils.extension

import android.os.SystemClock
import android.view.View
import java.util.WeakHashMap

/**
 * Created by Iftekhar on 2023/29/10.
 */
abstract class DebouncedOnClickListener(private val minInterval: Long = 600L) : View.OnClickListener {

    private val lastClickMap = WeakHashMap<View, Long>()

    abstract fun onDebouncedClick(v: View)

    override fun onClick(v: View) {
        val previousClickTimestamp = lastClickMap[v]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[v] = currentTimestamp
        if (previousClickTimestamp == null || currentTimestamp - previousClickTimestamp > minInterval) {
            onDebouncedClick(v)
        }
    }
}

fun View.setDebouncedOnClickListener(action: (() -> Unit)? = null) {
    setOnClickListener(object : DebouncedOnClickListener() {
        override fun onDebouncedClick(v: View) {
            action?.invoke()
        }
    })
}
