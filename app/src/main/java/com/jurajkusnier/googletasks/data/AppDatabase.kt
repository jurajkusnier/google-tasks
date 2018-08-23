package com.jurajkusnier.googletasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jurajkusnier.googletasks.R
import java.util.concurrent.Executors

@Database(entities = [TaskList::class, Task::class, Subtask::class],version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
    abstract fun getTaskListDao(): TaskListDao
    abstract fun getSubtaskDao(): SubtaskDao

    companion object {
        const val FIRST_ITEM_ID = 1
        val TAG = AppDatabase::class.java.simpleName
        private const val databaseName = "tasks.db"

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return instance ?: synchronized(this) {
                instance = buildPrePopulatedDatabase(context)
                instance ?: throw IllegalAccessException("Can't instantiate class $TAG")
            }
        }

        private fun buildPrePopulatedDatabase(context: Context):AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            val defaultTaskListName = context.getString(R.string.default_task_list_name)

                            Executors.newSingleThreadScheduledExecutor().execute {
                                getInstance(context).getTaskListDao().insert(TaskList(FIRST_ITEM_ID,defaultTaskListName))
                            }
                        }

                    })
                    .build()
    }

}