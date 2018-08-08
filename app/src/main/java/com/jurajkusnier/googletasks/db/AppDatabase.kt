package com.jurajkusnier.googletasks.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskList::class, Task::class, Subtask::class],version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
    abstract fun getTaskListDao(): TaskListDao
    abstract fun getSubtaskDao(): SubtaskDao

    companion object {

        private var db:AppDatabase? = null
        private const val databaseName = "tasks.db"

        fun getInstance(context: Context):AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            }

            return db ?: throw NullPointerException("Can't instantiate Database")
        }

    }

}