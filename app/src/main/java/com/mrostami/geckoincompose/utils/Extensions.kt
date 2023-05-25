package com.mrostami.geckoincompose.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mrostami.geckoincompose.R
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun Context.showToast(
    message: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, message, length).show()
}

fun Context.getColour(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

fun Activity.showSnack(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    actionText: String = "Ok",
    actionTextColorResId: Int = android.R.color.holo_red_dark,
    action: (() -> Unit)? = null
): Snackbar? {
    val view = findViewById<View>(android.R.id.content)
    view?.let {
        val snack = Snackbar.make(view, message, length)
        if (action != null) {
            snack.setAction(actionText) {
                action.invoke()
            }
            snack.setActionTextColor(ContextCompat.getColor(this, actionTextColorResId))
        }
        snack.show()
        return snack
    }
    return null
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

fun Long.decimalFormat(pattern: String = "#,###,###,###"): String? {
    val df = DecimalFormat(pattern)
    return try {
        df.format(this)
    } catch (e: Exception) {
        Log.e("DecimalFormat", e.message.toString())
        null
    }
}

fun Int.decimalFormat(pattern: String = "#,###,###,###"): String? {
    val df = DecimalFormat(pattern)
    return try {
        df.format(this)
    } catch (e: Exception) {
        Log.e("DecimalFormat", e.message.toString())
        null
    }
}

fun Double.toReadablePrice(): String {
    val price: Double = this
    return when {
        price in 1.0..10.0 -> {
            price.round(decimals = 3).toString()
        }
        price in 10.0..100.0 -> {
            price.round(decimals = 2).toString()
        }
        price in 100.0..1000.0 -> price.round(decimals = 1).toString()
        price > 1000.0 -> price.roundToInt().toString()
        else -> price.toString()
    }
}

@ColorInt
fun Context.getUpOrDownColor(change: Double): Int {
    val red: Int = getColour(R.color.down_red) ?: Color.parseColor("#D32F2F")
    val green: Int = getColour(R.color.up_green) ?: Color.parseColor("#00796B")
    return if (change >= 0) {
        green
    } else {
        red
    }
}

fun AppCompatTextView.applyPriceStateTextColor(change: Double?) {
    if (change == null) return
    val upOrDownColor: Int = context.getUpOrDownColor(change)
    setTextColor(upOrDownColor)
}