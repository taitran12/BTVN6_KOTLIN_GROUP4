package com.example.admin.lastExercise

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.admin.lastExercise.DAO.TaskDAO
import com.example.admin.lastExercise.DAO.UserDAO
import com.example.admin.lastExercise.Entity.Task
import com.example.admin.lastExercise.Entity.User

@Database(entities = arrayOf(Task::class, User::class), version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun taskDao() : TaskDAO
        abstract fun userDao() : UserDAO
        companion object {
            @Volatile
            private var instance: AppDatabase? = null
            private val LOCK = Any()

            // Singleton pattern
            operator fun invoke(context: Context) = instance
                    ?: synchronized(LOCK) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }

            private fun buildDatabase(context: Context) = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, DATABASE_NAME
            ).allowMainThreadQueries()
                    .build()
        }
    }