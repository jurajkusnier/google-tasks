package com.jurajkusnier.googletasks.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.SharedPreferencesHelper
import com.jurajkusnier.googletasks.db.TaskList
import com.jurajkusnier.googletasks.taskslist.TasksListRepository
import io.reactivex.disposables.Disposable

class TasksViewModel(private val tasksListRepository: TasksListRepository, preferencesHelper: SharedPreferencesHelper): ViewModel() {

    private val _currentTaskList = MutableLiveData<TaskList>()
    val currentTaskList: LiveData<TaskList>
        get() = _currentTaskList

    private var disposable: Disposable? = null

    init {
        disposable  = preferencesHelper.liveSelectedTaskList
                .flatMap {
                    selectedTaskListId ->
                        tasksListRepository.findTasksList(selectedTaskListId)
                }.subscribe {
                    selectedTaskList -> _currentTaskList.postValue(selectedTaskList)
                }
    }

    override fun onCleared() {
        disposable?.dispose()

        super.onCleared()
    }

}