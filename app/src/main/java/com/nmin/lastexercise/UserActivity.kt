package com.example.admin.lastExercise

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.admin.lastExercise.Adapter.AdapterUser
import com.example.admin.lastExercise.DAO.TaskDAO
import com.example.admin.lastExercise.DAO.UserDAO
import com.example.admin.lastExercise.Entity.User
import com.example.admin.lastExercise.Interface.IListenerDeleteUser
import com.nmin.lastexercise.R
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity(), IListenerDeleteUser {
    private lateinit var listUser : List<User>
    private lateinit var userDAO: UserDAO
    private lateinit var taskDAO : TaskDAO
    private lateinit var db : AppDatabase
    private lateinit var adapterUser: AdapterUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar?.setTitle(R.string.user)
        db = AppDatabase.invoke(this) // get Room database instance
        userDAO = db.userDao()
        taskDAO= db.taskDao()
        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) // set layoutmanager
        recyclerViewUser.layoutManager = layoutManager
        setAdapter()
        addEvent()
    }

    private fun addEvent() {
        buttonAdd.setOnClickListener {
            val userName = editUserName.text.toString()
            if(!userName.isEmpty()) { // Add new user
                val obj = User(null, userName)
                userDAO.insert(obj)
                setAdapter()
                editUserName.text = null
            }
            else {
                Toast.makeText(this, "Please enter user name!!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setAdapter() {
        listUser = userDAO.getAll() // get infor user from DB
        val arrCountAssign = ArrayList<Int>()
        for(i in 0..listUser.size - 1) {
            arrCountAssign.add(taskDAO.getByIdUser(listUser[i].id!!))
            Log.i("count", ""+arrCountAssign[i])
        }
        adapterUser = AdapterUser(listUser, arrCountAssign, this)
        recyclerViewUser.adapter = adapterUser
    }

    override fun deleteUser(id_user: Int) {
        userDAO.delete(id_user)
        setAdapter()
        taskDAO.unAssignedByIdUser(id_user)
    }
}
