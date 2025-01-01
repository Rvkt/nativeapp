package com.softmintindia.pgsdk.Utils

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.widget.Toast

fun copyToClipboard(context: Context, label: String, value: String) {
    // Get the clipboard manager
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // Create a new clip with the UPI string
    val clip = ClipData.newPlainText(label, value)
    // Set the clip in the clipboard
    clipboard.setPrimaryClip(clip)
    // Show a toast message to notify the user
    Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
}
