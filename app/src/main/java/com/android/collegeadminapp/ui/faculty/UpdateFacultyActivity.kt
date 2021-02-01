package com.android.collegeadminapp.ui.faculty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.collegeadminapp.R
import com.android.collegeadminapp.databinding.ActivityUpdateFacultyBinding
import com.android.collegeadminapp.ui.faculty.AddUpdateFacultyActivity.Companion.IS_ADD
import com.android.collegeadminapp.ui.faculty.AddUpdateFacultyActivity.Companion.UPDATE_OBJ
import com.android.collegeadminapp.util.FireBaseConstants.FB_FACULTY
import com.android.collegeadminapp.util.FireBaseConstants.FB_FACULTY_CSE
import com.android.collegeadminapp.util.FireBaseConstants.FB_FACULTY_ECE
import com.android.collegeadminapp.util.FireBaseConstants.FB_FACULTY_ME
import com.android.collegeadminapp.util.hide
import com.android.collegeadminapp.util.show
import com.android.collegeadminapp.util.toast
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class UpdateFacultyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateFacultyBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_faculty)

        init()

        rv()

        binding.fabAddFaculty.setOnClickListener {
            Intent(this, AddUpdateFacultyActivity::class.java).apply {
                this.putExtra(IS_ADD, true)
                startActivity(this)
            }
        }
    }

    private fun rv() {
        //todo in single rv
        lifecycleScope.launch {
            setRv(FB_FACULTY_CSE, binding.rvDepartmentCS, binding.CSNoData)
            setRv(FB_FACULTY_ECE, binding.rvDepartmentECE, binding.ECENoData)
            setRv(FB_FACULTY_ME, binding.rvDepartmentME, binding.MENoData)
        }
    }

    private suspend fun setRv(department: String, rv: RecyclerView, noData: View) {
        var list: MutableList<Faculty>
        val adapter = FacultyAdapter()
        val dbPath = databaseReference.child(department)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list = mutableListOf()
                if (!snapshot.exists()) {
                    noData.show()
                    rv.hide()
                } else {
                    noData.hide()
                    rv.show()
                    snapshot.children.forEach { snap ->
                        list.add(snap.getValue(Faculty::class.java)!!)
                    }
                    adapter.addItems(list)
                    setUpRv(rv, adapter)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                this@UpdateFacultyActivity.toast(error.toString())
            }
        }
        dbPath.addValueEventListener(listener)
    }

    private fun setUpRv(rv: RecyclerView, adapter: FacultyAdapter) {
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this@UpdateFacultyActivity)
        rv.adapter = adapter

        adapter.listener = {_,faculty,_ ->
            Intent(this,AddUpdateFacultyActivity::class.java).apply {
                this.putExtra(IS_ADD,false)
                this.putExtra(UPDATE_OBJ,faculty)
                startActivity(this)
            }
        }
    }

    private fun init() {
        databaseReference = FirebaseDatabase.getInstance().reference.child(FB_FACULTY)
    }
}