package com.example.admin.lastExercise

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.admin.lastExercise.Adapter.AdapterTask
import com.example.admin.lastExercise.DAO.TaskDAO
import com.example.admin.lastExercise.Entity.Task
import com.nmin.lastexercise.R
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var listTask : List<Task>
    private lateinit var taskDao : TaskDAO
    private lateinit var adapterTask: AdapterTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        supportActionBar?.setTitle(R.string.task)
        initRoomDatabase()

        listTask = taskDao.getAll() // get infor from DB

        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerViewTask.layoutManager = layoutManager
        setAdapter(listTask) // set adapter
        addEvent()
    }

    private fun addEvent() {
        buttonAdd.setOnClickListener(this)
        buttonListUser.setOnClickListener(this)
        swipeRFLayout.setOnRefreshListener {
            listTask = taskDao.getAll()
            setAdapter(listTask)
        }
        radioGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radioButtonAll) {
                listTask = taskDao.getAll()
                setAdapter(listTask)
            }
            else if(checkedId == R.id.radioButtonCompleted) {
                listTask = taskDao.getByCompleted(true)
                setAdapter(listTask)
            }
            else if (checkedId == R.id.radioButtonUncompleted) {
                listTask = taskDao.getByCompleted(false)
                setAdapter(listTask)
            }
        }
    }

    private fun initRoomDatabase() {
        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries()
                .build()
        taskDao = db.taskDao()
    }

    private fun setAdapter(listTask : List<Task>) {
        adapterTask = AdapterTask(listTask)
        recyclerViewTask.adapter = adapterTask
        swipeRFLayout.isRefreshing = false
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.buttonAdd -> {
                val title = editTitle.text.toString()
                if(!title.isEmpty()) { // Add new task
                    val obj : Task = Task(null, title, false, null)
                    taskDao.insert(obj)
                    listTask = taskDao.getAll()
                    setAdapter(listTask)
                    editTitle.text = null
                }
                else {
                    Toast.makeText(this, "Please enter title!!!", Toast.LENGTH_LONG).show()
                }
            }
            R.id.buttonListUser -> {
                val intent = Intent(this@TaskActivity, UserActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
