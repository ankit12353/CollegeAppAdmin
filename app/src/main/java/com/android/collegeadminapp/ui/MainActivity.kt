package com.android.collegeadminapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.android.collegeadminapp.R
import com.android.collegeadminapp.databinding.ActivityMainBinding
import com.android.collegeadminapp.ui.faculty.UpdateFacultyActivity
import com.android.collegeadminapp.ui.notice.DeleteNoticeActivity
import com.android.collegeadminapp.ui.notice.UploadNoticeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.layoutUploadNotice.setOnClickListener {
            startActivity(Intent(this, UploadNoticeActivity::class.java))
        }

        binding.layoutUploadImage.setOnClickListener {
            startActivity(Intent(this, UploadImageActivity::class.java))
        }

        binding.layoutUploadPdf.setOnClickListener {
            startActivity(Intent(this, UploadPdfActivity::class.java))
        }

        binding.layoutUpdateFaculty.setOnClickListener {
            startActivity(Intent(this, UpdateFacultyActivity::class.java))
        }
        binding.layoutDeleteNotice.setOnClickListener {
            startActivity(Intent(this, DeleteNoticeActivity::class.java))
        }
    }
}