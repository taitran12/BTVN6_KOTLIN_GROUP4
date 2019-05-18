package com.example.admin.lastExercise.Entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

//@Entity(foreignKeys = arrayOf(ForeignKey(
//                        entity = User::class,
//                        parentColumns = arrayOf("id"),
//                        childColumns = arrayOf("id_user"))))
@Entity
data class Task(
        @PrimaryKey(autoGenerate = true)
        var id : Int? = null,
        var description : String,
        var completed : Boolean,
        var id_user : Int? = null
//        @ColumnInfo(name = "id_user") var id_user : Int? = null
)