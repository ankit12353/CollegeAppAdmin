package com.android.collegeadminapp.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.android.collegeadminapp.R
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun uploadTask(bitmap: Bitmap, storageReference: StorageReference, pathString: String): UploadTask {
    //todo
    val bos: ByteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
    val finalImage = bos.toByteArray()
    val filePath = storageReference.child(pathString).child("${finalImage}jpg")
    return filePath.putBytes(finalImage)
}

fun Context.progressBar(layout: LinearLayout): ProgressBar {
    //Todo progressbar dynamically without view
    ProgressBar(this).apply {
        this.layoutParams = LinearLayout.LayoutParams(
            150, 150
        )
        this.background =
            ResourcesCompat.getDrawable(resources, R.drawable.custom_progressbar, null)
        layout.addView(this)
        this.hide()
        return this
    }
}

fun Context.getSelectedGalleryBitmap(uri: Uri?): Bitmap? {
    //todo remove deprecation
    var bitmap: Bitmap? = null
    try {
        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}
//

fun Context.spinner(spinnerItem: Array<String>, spinner: Spinner, itemClick: (String) -> Unit) {
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItem).also {
        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
    spinner.apply {
        this.adapter = adapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemClick(spinnerItem[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
}

fun Context.confirmationAlertDialog(title: String, message: String): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)
        setIcon(android.R.drawable.ic_dialog_alert)
        setNegativeButton(android.R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
    }
    return builder
}

fun Context.getPdfName(pdfData: Uri?): String {
    var pdfName = ""
    if (pdfData!!.toString().startsWith("content://")) {
        var cursor: Cursor? = null
        try {
            cursor = this.contentResolver.query(pdfData, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else if (pdfData.toString().startsWith("file://")) {
        pdfName = File(pdfData.toString()).name
    }
    return pdfName
}

fun Activity.openGallery(req_code: Int) {
    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
        startActivityForResult(this, req_code)
    }
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Bitmap) {
    imageView.setImageBitmap(resource)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun getCurrentDate(): String {
    val calForDate = Calendar.getInstance()
    val currentDate = SimpleDateFormat("dd-MM-yy")
    return currentDate.format(calForDate.time)
}

fun getCurrentTime(): String {
    val calForTime = Calendar.getInstance()
    val currentTime = SimpleDateFormat("hh:mm a")
    return currentTime.format(calForTime.time)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}