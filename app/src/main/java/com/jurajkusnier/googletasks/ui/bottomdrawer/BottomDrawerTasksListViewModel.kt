package com.jurajkusnier.googletasks.ui.bottomdrawer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.data.PreferencesDataStore
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.data.TasksListRepository
import io.reactivex.disposables.Disposable

class BottomDrawerTasksListViewModel(private val tasksListRepository: TasksListRepository, private val preferencesDataStore: PreferencesDataStore) : ViewModel() {

    private val _tasksList:MutableLiveData<List<TaskList>> = MutableLiveData()
    val taskList:LiveData<List<TaskList>>
        get() = _tasksList

    private var disposable:Disposable? = null

    init {
        disposable = tasksListRepository.getTasksList()?.subscribe {
            _tasksList.postValue( it)
        }
    }

    var selectedTaskList
        get() = preferencesDataStore.selectedTaskList
        set(value) { preferencesDataStore.selectedTaskList = value }

    override fun onCleared() {
        disposable?.dispose()

        super.onCleared()
    }
}