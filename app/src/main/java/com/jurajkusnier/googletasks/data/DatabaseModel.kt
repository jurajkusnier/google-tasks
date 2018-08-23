package com.jurajkusnier.googletasks.data

import androidx.room.*
import java.util.*


@Entity
data class TaskList
(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "ID")
        val id:Int = 0,
        @ColumnInfo(name = "LIST_NAME")
        val listName:String

) {
        @Ignore
        constructor(listName: String): this(0, listName)
}

@Entity(foreignKeys = [
                ForeignKey(
                        entity = TaskList::class,
                        parentColumns = ["ID"],
                        childColumns =["TASK_LIST_ID"],
                        onDelete = ForeignKey.CASCADE
                )
        ])
data class Task(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "ID")
        val id:Int = 0,
        @ColumnInfo(name = "TASK_LIST_ID")
        val taskListId:Int,
        @ColumnInfo(name = "TASK_NAME")
        val taskName:String,
        @ColumnInfo(name = "TASK_DETAILS")
        val taskDetails:String = "",
        @ColumnInfo(name = "TASK_DATE")
        val taskDate:Date? = null,
        @ColumnInfo(name = "COMPLETED")
        val taskCompleted:Boolean = false

) {
        @Ignore
        constructor(taskListId: Int, taskName: String, taskDetails: String ="", taskDate: Date? = null, taskCompleted: Boolean = false): this(0,taskListId,taskName,taskDetails,taskDate, taskCompleted)

}

@Entity(foreignKeys = [
        ForeignKey(
                entity = Task::class,
                parentColumns = ["ID"],
                childColumns =["TASK_ID"],
                onDelete = ForeignKey.CASCADE
        )
])
data class Subtask (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "ID")
        val id:Int = 0,
        @ColumnInfo(name = "TASK_ID")
        val taskId:Int,
        @ColumnInfo(name = "TASK_NAME")
        val subtaskName:String,
        @ColumnInfo(name = "COMPLETED")
        val taskCompleted:Boolean = false
) {
        @Ignore
        constructor(taskId:Int, taskName: String, taskCompleted: Boolean = false): this(0, taskId, taskName, taskCompleted)

}


