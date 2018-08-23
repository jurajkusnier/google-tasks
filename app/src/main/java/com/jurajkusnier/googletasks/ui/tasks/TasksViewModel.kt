package com.jurajkusnier.googletasks.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.data.PreferencesDataStore
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.data.TasksListRepository
import io.reactivex.disposables.Disposable

class TasksViewModel(private val tasksListRepository: TasksListRepository, preferencesDataStore: PreferencesDataStore): ViewModel() {

    private val _currentTaskList = MutableLiveData<TaskList>()
    val currentTaskList: LiveData<TaskList>
        get() = _currentTaskList

    private var disposable: Disposable? = null

    init {
        disposable  = preferencesDataStore.liveSelectedTaskList
                .switchMap{
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