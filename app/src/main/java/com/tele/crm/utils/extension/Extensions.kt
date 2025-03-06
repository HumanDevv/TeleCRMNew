package com.tele.crm.utils.extension

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.Parcelable
import android.provider.CallLog
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern


@Suppress("unused")
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

@Suppress("unused")
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

@SuppressLint("all")
fun getDeviceId(context: Context): String =
    Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)


fun String.capitalizeNew(): String {
    return split("\\s+".toRegex()).joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}


@Suppress("unused")
val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

@Suppress("unused")
val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()

private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "US")).configure()

private fun NumberFormat.configure() = apply {
    maximumFractionDigits = 2
    minimumFractionDigits = 2
}

fun Number.asCurrency(): String {
    return currencyFormatter.format(this).replace("$", "").clean()
}

fun String.clean(clearZero:Boolean=true): String {
    var value = this
    if (value.endsWith(".00")) {
        value = value.removeSuffix(".00")
    }
    if (value.endsWith(".0")) {
        value = value.removeSuffix(".0")
    }
    if (clearZero && value.startsWith("0")) {
        value = value.removePrefix("0")
    }
    return value
}

fun String.replaceFirstChar() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


fun String.isValidEmail() =
    TextUtils.isEmpty(this).not() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidMobile(): Boolean {
    return this.length in 8..13 && Patterns.PHONE.matcher(this).matches()
}

fun isUpperAndLowerCharacter(password: String): Boolean {
    val pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z]).+\$")
    val matcher: Matcher = pattern.matcher(password)
    return matcher.matches()
}

fun isNumericAndSpecialCharacter(password: String): Boolean {
    val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[@#\$%*^&+=!]).+\$")
    val matcher: Matcher = pattern.matcher(password)
    return matcher.matches()
}

fun isValidEmail(target: CharSequence): Boolean {
    return TextUtils.isEmpty(target).not() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int?.defaultOnNullValue(): Int = this ?: 0
fun Double?.defaultOnNullValue(): Double = this ?: 0.0
fun Float?.defaultOnNullValue(): Float = this ?: 0.0f
fun Boolean?.defaultOnNullValue(): Boolean = this ?: false
fun <T> List<T>?.defaultOnNullValue(): List<T> = this ?: emptyList()

fun regexMatcher(string: String): Boolean {
    val m = Pattern.compile("(([A-Za-z]{3})([0-9]{4}))").matcher(string)
    return m.find() && m.group(0) != null
}

fun Context.getCompatColor(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)


/*fun datePicker(
    context: Context,
    currentDate: String,
    minDate: String?,
    maxDate: String?,
    format: String = "",
    onSelect: (String,String) -> Unit
) {
    var myFormatTwo = "yyyy-MM-dd"
    val myFormat = "yyyy-MM-dd"
    if (format.isNotEmpty())
        myFormatTwo = format
    val sdfSet = SimpleDateFormat(myFormatTwo, Locale.getDefault())
    val sdfReturn = SimpleDateFormat(myFormat, Locale.getDefault())

    val myCalendar = Calendar.getInstance()
    if (currentDate != "") {
        val d = sdfSet.parse(currentDate)
        myCalendar.time = d ?: Date()
    }


    val date =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth

            val dateSet = sdfSet.format(myCalendar.time)
            val dateReturn = sdfReturn.format(myCalendar.time)
            onSelect(dateSet,dateReturn)

        }
    val datePicker = DatePickerDialog(
        context,
        R.style.DialogTheme,
        date,
        myCalendar[Calendar.YEAR],
        myCalendar[Calendar.MONTH],
        myCalendar[Calendar.DAY_OF_MONTH]
    )


    try {

        if (minDate != null) {
            val d = sdfReturn.parse(minDate)
            datePicker.datePicker.minDate = d!!.time
        }
        if (maxDate != null) {
            val d = sdfSet.parse(maxDate)
            datePicker.datePicker.maxDate = d!!.time
        }
    } catch (e: Exception) {
        e.fillInStackTrace()
    }


    datePicker.show()
    datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(ContextCompat.getColor(context, R.color.blue))
    datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(ContextCompat.getColor(context, R.color.blue))

}*/


fun showTimePicker(
    context: Context,
    time:String = "",
    onSelect: (String) -> Unit = { _ -> }) {
    val c = Calendar.getInstance()
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

    if (time != "") {
        val d = sdf.parse(time)
        c.time = d ?: Date()
    }
    // on below line we are getting our hour, minute.
    val hour = c[Calendar.HOUR_OF_DAY]
    val minute = c[Calendar.MINUTE]

    // on below line we are initializing Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // on below line we are setting selected time in our text view.

            val time11 = formatRatioToTime(hourOfDay, minute)
            onSelect(time11)
        },
        hour,
        minute,
        true
    )

    // at last we are calling show to display our time picker dialog.
    timePickerDialog.show()
}

fun formatRatioToTime(hours: Int, minutes: Int): String {
    return String.format("%02d:%02d", hours, minutes)
}

fun convertToAMPM(time24Hour: String?): String {
    if(time24Hour.isNullOrEmpty())
        return  ""
    var timeAMPM = ""
    try {
        val sdf24 = SimpleDateFormat("HH:mm",Locale.getDefault())
        val sdf12 = SimpleDateFormat("hh:mm a",Locale.getDefault())
        val date = sdf24.parse(time24Hour)
        timeAMPM = sdf12.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        return  ""
    }
    return timeAMPM
}


fun Context.shareLinkOnNumber(subject: String, number: String) {

    val uri = Uri.parse("smsto:$number")
    val intent = Intent(Intent.ACTION_SENDTO, uri)
    intent.putExtra(
        "sms_body",
        subject
    )
    startActivity(intent)

}

@SuppressLint("NewApi")
fun getTimeAgo(isoDate: String): String {
    val now = Instant.now()  // Current UTC time
    val past = Instant.parse(isoDate)  // Parse API date

    val seconds = ChronoUnit.SECONDS.between(past, now)
    val minutes = ChronoUnit.MINUTES.between(past, now)
    val hours = ChronoUnit.HOURS.between(past, now)
    val days = ChronoUnit.DAYS.between(past, now)

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> "${days / 7} weeks ago"
    }


}
fun showDatePicker(context: Context, selectedDate: String?, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) // Ensure same format as selectedDate

    selectedDate?.let {
        try {
            val date = sdf.parse(it)
            if (date != null) {
                calendar.time = date // Correctly update calendar
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedCalendar.time)

            onDateSelected(formattedDate)
        },
        year, month, day
    )

    // Ensure minDate works properly
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}



fun getCallLogsForNumber(context: Context, phoneNumber: String): List<CallLogItem> {
    val callLogs = mutableListOf<CallLogItem>()

    val normalizedNumber = normalizePhoneNumber(phoneNumber)

    val projection = arrayOf(
        CallLog.Calls.NUMBER,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE // Fetch timestamp
    )

    val selection = "${CallLog.Calls.NUMBER} LIKE ?"
    val selectionArgs = arrayOf("%$normalizedNumber%")

    val cursor: Cursor? = context.contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${CallLog.Calls.DATE} DESC"
    )

    cursor?.use {
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
        val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)

        while (it.moveToNext()) {
            val number = it.getString(numberIndex)
            val duration = it.getInt(durationIndex) // Convert to integer
            val timestamp = it.getLong(dateIndex) // Get timestamp

            val type = when (it.getInt(typeIndex)) {
                CallLog.Calls.INCOMING_TYPE -> "incoming"
                CallLog.Calls.OUTGOING_TYPE -> "outgoing"
                CallLog.Calls.MISSED_TYPE -> "missed"
                CallLog.Calls.REJECTED_TYPE -> "rejected"
                else -> "Unknown"
            }

            callLogs.add(CallLogItem(number, duration, type, timestamp))
        }
    }

    cursor?.close()
    return callLogs
}
/**
 * Normalize phone number by removing country code (+91 or 91) if present.
 */
fun normalizePhoneNumber(phoneNumber: String): String {
    return phoneNumber.replace("^\\+?91".toRegex(), "").trim()
}

fun getFormattedTimestamp(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC") // Ensure it parses UTC time correctly
        }
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val date = inputFormat.parse(timestamp) // Convert string to Date
        date?.let { outputFormat.format(it) } ?: "Invalid date"
    } catch (e: Exception) {
        "Invalid date"
    }
}