package com.example.ica2helper.Components
/** Adapted from https://github.com/ml11109/ICA2-Prep/ **/
import android.content.Context
import android.content.Intent
import android.net.Uri

fun sendEmail(context: Context, emailAddress: String, emailSubject: String, emailText: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, emailAddress)
        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        putExtra(Intent.EXTRA_TEXT, emailText)
    }
    context.startActivity(intent)
}