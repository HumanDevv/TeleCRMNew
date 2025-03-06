package com.tele.crm.utils.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.tele.crm.MainActivity
import com.tele.crm.R

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt


fun ViewGroup.inflate(@LayoutRes resourceId: Int): View =
    LayoutInflater.from(context).inflate(resourceId, this, false)



inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}

fun View.visible(animate: Boolean = false) {
    if (animate) {
        animate().alpha(1f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else {
        visibility = View.VISIBLE
    }
}
/*

fun Int.getString(): String {
    return MyApp.getContext()?.getString(this).orEmpty()
}
*/

fun View.invisible(animate: Boolean = false) {
    hide(View.INVISIBLE, animate)
}

fun View.gone(animate: Boolean = false) {
    hide(View.GONE, animate)
}

fun Activity.castActivity(): MainActivity {
    return this as MainActivity
}

fun View.visibleOrGone(show: Boolean, animate: Boolean = false) {
    if (show) visible(animate) else gone(animate)
}

private fun View.hide(hidingStrategy: Int, animate: Boolean = false) {
    if (animate) {
        animate().alpha(0f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy
            }
        })
    } else {
        visibility = hidingStrategy
    }
}


fun View.breakDownVisibility(breakdownIv: AppCompatImageView) {
    if (isVisible) {
        gone(true)
        breakdownIv.rotation = 0f
    } else {
        breakdownIv.rotation = 180f
        visible(true)
    }
}



fun AppCompatEditText.passwordShowHide(
    showPassword: Boolean, passwordShowHide: AppCompatImageView
) {
    if (showPassword) {
        transformationMethod = PasswordTransformationMethod()
        passwordShowHide.setImageResource(R.drawable.ic_launcher_background)
        setSelection(text.toString().length)
    } else {
        transformationMethod = null
        passwordShowHide.setImageResource(R.drawable.ic_launcher_background)
        setSelection(text.toString().length)
    }
}


fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMillis)
        block()
    }
}


fun TextView.makeTextLink(str: String,
                          underlined: Boolean = false,
                          color: Int? = this.context.getColorCompat(R.color.blue),
                          action: (() -> Unit)? = null) {

    val spannableString = SpannableString(this.text)
    val textColor = color ?: this.currentTextColor
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            action?.invoke()
        }

        override fun updateDrawState(drawState: TextPaint) {
            super.updateDrawState(drawState)
            drawState.isUnderlineText = underlined
            drawState.color = textColor
        }
    }
    val index = spannableString.indexOf(str)
    spannableString.setSpan(
        clickableSpan,
        index,
        index + str.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannableString
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
}


fun TextView.setTextColorCompat(colorResId: Int) {
    val color = ContextCompat.getColor(context, colorResId)
    setTextColor(color)
}

val EditText.value get() = text?.toString()?.trim() ?: ""



fun TextView.setResizableText(
    fullText: String,
    maxLines: Int,
    viewMore: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
) {
    val width = width
    if (width <= 0) {
        post {
            setResizableText(fullText, maxLines, viewMore, applyExtraHighlights)
        }
        return
    }
    movementMethod = LinkMovementMethod.getInstance()
    // Since we take the string character by character, we don't want to break up the Windows-style
    // line endings.
    val adjustedText = fullText.replace("\r\n", "\n")
    // Check if even the text has to be resizable.
    val textLayout = StaticLayout(
        adjustedText,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
        // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights)
        return
    }
    val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
    val suffixText =
        if (viewMore) resources.getString(R.string.read_more) else resources.getString(R.string.show_less)
    var charactersToTake = charactersAtLineEnd - suffixText.length / 2 // Good enough first guess
    if (charactersToTake <= 0) {
        // Happens when text is empty
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            null,
            viewMore,
            applyExtraHighlights)
        return
    }
    if (!viewMore) {
        // We can set the text immediately because nothing needs to be measured
        val htmlText = adjustedText.replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            fullText,
            maxLines,
            HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
            suffixText,
            viewMore,
            applyExtraHighlights
        )
        return
    }
    val lastHasNewLine =
        adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1)).contains("\n")
    val linedText = if (lastHasNewLine) {
        val charactersPerLine = textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
        val lineOfSpaces = "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
        charactersToTake += lineOfSpaces.length - 1
        adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                adjustedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1)).replace("\n", lineOfSpaces) +
                adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
    } else {
        adjustedText
    }
    // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
    val shortenedString = linedText.take(charactersToTake)
    val shortenedStringWithSuffix = shortenedString + suffixText
    val shortenedStringWithSuffixLayout = StaticLayout(
        shortenedStringWithSuffix,
        paint,
        width - paddingLeft - paddingRight,
        Layout.Alignment.ALIGN_NORMAL,
        lineSpacingMultiplier,
        lineSpacingExtra,
        includeFontPadding
    )
    val modifier: Int
    if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
        modifier = 1
        charactersToTake-- // We might just be at the right position already
    } else {
        modifier = -1
    }
    do {
        charactersToTake += modifier
        val baseString = linedText.take(charactersToTake)
        val appended = baseString + suffixText
        val newLayout = StaticLayout(
            appended,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )
    } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
        (modifier > 0 && newLayout.getLineEnd(maxLines - 1) > appended.length)
    )
    if (modifier > 0) {
        charactersToTake-- // We went overboard with 1 char, fixing that
    }
    // We need to convert newlines because we are going over to HTML now
    val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
    text = addClickablePartTextResizable(
        fullText,
        maxLines,
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
        suffixText,
        viewMore,
        applyExtraHighlights
    )
}


private fun TextView.addClickablePartTextResizable(
    fullText: String,
    maxLines: Int,
    shortenedText: Spanned,
    clickableText: String?,
    viewMore: Boolean,
    applyExtraHighlights: ((Spannable) -> (Spannable))? = null
): Spannable {
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        builder.append(clickableText)
        val startIndexOffset = if (viewMore) 1 else 0 // Do not highlight the 3 dots and the space
        builder.setSpan(object : NoUnderlineClickSpan(context) {
            override fun onClick(widget: View) {
                if (viewMore) {
                    setResizableText(fullText, maxLines, false, applyExtraHighlights)
                } else {
                    setResizableText(fullText, maxLines, true, applyExtraHighlights)
                }
            }
        }, builder.indexOf(clickableText) + startIndexOffset, builder.indexOf(clickableText) + clickableText.length, 0)
    }
    if (applyExtraHighlights != null) {
        return applyExtraHighlights(builder)
    }
    return builder
}

open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = ContextCompat.getColor(context, R.color.blue)
    }

    override fun onClick(widget: View) {
        //onClick
    }
}


fun View.getFileFromView(): File {
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable = background
    if (bgDrawable != null) bgDrawable.draw(canvas)
    else canvas.drawColor(Color.WHITE)
    draw(canvas)

    val file: File = File.createTempFile("cupify",".jpg",context.cacheDir)
    val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
    returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
    os.close()

    return file
}


fun Bitmap.getFileFromBitmap(context:Context): File {

    val time= System.currentTimeMillis()
    val file: File = File.createTempFile("cupify_$time",".jpg",context.cacheDir)
    val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
    this.compress(Bitmap.CompressFormat.JPEG, 100, os)
    os.close()

    return file
}

fun String.containsHtml(): Boolean {
    val htmlPattern = ".*\\<[^>]+>.*"
    return this.matches(htmlPattern.toRegex())
}

fun Context.openWhatsAppChat(phoneNumber: String, message: String) {
    // Ensure the phone number starts with +91
    val formattedPhoneNumber = if (phoneNumber.startsWith("+91")) phoneNumber else "+91$phoneNumber"

    val url = "https://api.whatsapp.com/send?phone=$formattedPhoneNumber&text=${Uri.encode(message)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    try {
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "Error opening WhatsApp!", Toast.LENGTH_SHORT).show()
    }
}



