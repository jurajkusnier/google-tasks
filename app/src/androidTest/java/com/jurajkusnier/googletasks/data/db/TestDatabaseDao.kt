package com.jurajkusnier.googletasks.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.jurajkusnier.googletasks.data.AppDatabase
import com.jurajkusnier.googletasks.data.Subtask
import com.jurajkusnier.googletasks.data.Task
import com.jurajkusnier.googletasks.data.TaskList
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class TestDatabaseDao {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mDb: AppDatabase

    private val myTaskList  = TaskList(1,"My Task List")
    private val myTask = Task(1, myTaskList.id,"My Task")
    private val mySubTask = Subtask(1,"My Subtask")

    private lateinit var mDate:Date

    @Before
    fun setupDatabase() {
        val calendar = Calendar.getInstance()
        calendar.set(2018,11,1)
        mDate = calendar.time

        val context = InstrumentationRegistry.getTargetContext()
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        mDb.close()
    }

    @Test
    fun listAllTasks_whenEmptyDatabase() {
        mDb.getTaskDao().listAllTasks().test().assertValue { it.isEmpty() }
    }

    @Test
    fun insertTask_whenTaskIsDataSource() {

        mDb.getTaskListDao().insert(myTaskList)
        mDb.getTaskDao().insert(myTask)

        val newTask =  myTask.copy(taskName = "Edited Name")

        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue(listOf(myTask))

        mDb.getTaskDao().insert(newTask)

        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue(listOf(newTask))
    }

    @Test
    fun insertMultipleTasksIntoTaskList() {

        val taskA = Task(1,"A",taskDate = mDate,taskCompleted = true)
        val taskB = Task(1,"B",taskCompleted = false)
        val taskC = Task(1,"C",taskDate = mDate,taskDetails = "12345")

        mDb.getTaskListDao().insert(myTaskList)
        mDb.getTaskDao().insert(taskA)
        mDb.getTaskDao().insert(taskB)
        mDb.getTaskDao().insert(taskC)

        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue { it.size == 3 }
    }

    @Test
    fun deleteTask_should_deleteChildSubtasks() {

        val anotherTask = Task(2, myTaskList.id,"Another Task")
        val anotherSubtask = Subtask(anotherTask.id,"Another Subtask")

        mDb.getTaskListDao().insert(myTaskList)
        mDb.getTaskDao().insert(myTask)
        mDb.getTaskDao().insert(anotherTask)
        mDb.getSubtaskDao().insert(mySubTask)
        mDb.getSubtaskDao().insert(anotherSubtask)

        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue(listOf(myTask,anotherTask))
        mDb.getSubtaskDao().findSubtasksFromTask(myTask.id).test().assertValue {
            it.size == 1
        }

        mDb.getTaskDao().delete(myTask)

        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue(listOf(anotherTask))
        mDb.getSubtaskDao().findSubtasksFromTask(myTask.id).test().assertValue(emptyList())
        mDb.getSubtaskDao().findSubtasksFromTask(anotherTask.id).test().assertValue {
            it.size == 1 && it[0].subtaskName == anotherSubtask.subtaskName
        }

    }


    @Test
    fun deleteTaskList_should_deleteChildTasks() {

        val anotherTaskList  = TaskList(2,"Another Task List")

        val taskA1 = Task(myTaskList.id,"A1","", mDate,true)
        val taskA2 = Task(myTaskList.id,"A2",taskDate = mDate)
        val taskB1 = Task(anotherTaskList.id,"B1","")
        val taskB2 = Task(anotherTaskList.id,"B2","123",null,true)

        mDb.getTaskListDao().insert(myTaskList)
        mDb.getTaskListDao().insert(anotherTaskList)

        mDb.getTaskDao().insert(taskA1)
        mDb.getTaskDao().insert(taskA2)
        mDb.getTaskDao().insert(taskB1)
        mDb.getTaskDao().insert(taskB2)

        mDb.getTaskListDao().getTaskLists().test().assertValue(listOf(myTaskList,anotherTaskList))
        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue { it.size == 2}
        mDb.getTaskDao().findTasksFromList(anotherTaskList.id).test().assertValue { it.size == 2}

        mDb.getTaskListDao().delete(myTaskList)

        mDb.getTaskListDao().getTaskLists().test().assertValue(listOf(anotherTaskList))
        mDb.getTaskDao().findTasksFromList(myTaskList.id).test().assertValue { it.isEmpty()}
        mDb.getTaskDao().findTasksFromList(anotherTaskList.id).test().assertValue { it.size == 2}

    }

}