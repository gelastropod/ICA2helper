package com.example.ica2helper.Components

import android.content.Context
import android.util.Log
import java.io.InputStreamReader
import java.io.BufferedReader

fun readRawTextFile(context: Context, resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val stringBuilder = StringBuilder()

    var line: String?
    try {
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
        }
    } catch (e: Exception) {
        Log.e("FileReadError", "Error reading raw file: ${e.message}")
    } finally {
        try {
            reader.close()
        } catch (e: Exception) {
            Log.e("FileReadError", "Error closing reader: ${e.message}")
        }
    }

    return stringBuilder.toString()
}