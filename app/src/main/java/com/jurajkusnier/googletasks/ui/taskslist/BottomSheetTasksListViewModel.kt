package com.jurajkusnier.googletasks.ui.taskslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.taskslist.TasksListRepository
import io.reactivex.disposables.Disposable

class BottomSheetTasksListViewModel(tasksListRepository: TasksListRepository) : ViewModel() {

    private val _tasksList:MutableLiveData<List<TaskList>> = MutableLiveData()
    val taskList:LiveData<List<TaskList>>
        get() = _tasksList

    private var disposable:Disposable? = null

    init {
        disposable = tasksListRepository.getTasksList()?.subscribe {
            _tasksList.postValue( it)
        }
    }

    override fun onCleared() {
        disposable?.dispose()

        super.onCleared()
    }
}