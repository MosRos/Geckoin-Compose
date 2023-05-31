package com.mrostami.geckoincompose.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import com.mrostami.geckoincompose.GeckoinApplication


fun dpToPx(dp: Float) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        GeckoinApplication.getInstance().getAppContext().resources.displayMetrics
    )

fun dpToPx(context: Context, dp: Float) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )

fun openLinkInBrowser(context: Context, url: String) {
    val safeUrl: String = if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "http://$url"
    } else {
        url
    }
    val intent =
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(safeUrl)
        }
    context.startActivity(intent)
}