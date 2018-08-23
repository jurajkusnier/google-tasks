package com.jurajkusnier.googletasks.ui.bottombarmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jurajkusnier.googletasks.data.PreferencesDataStore
import com.jurajkusnier.googletasks.data.TaskList
import com.jurajkusnier.googletasks.data.TasksListRepository
import io.reactivex.disposables.Disposable

class BottomBarMenuViewModel(private val tasksListRepository: TasksListRepository, private val preferencesDataStore: PreferencesDataStore):ViewModel() {

    private val _currentTaskList = MutableLiveData<TaskList>()
    val currentTaskList:LiveData<TaskList>
        get() = _currentTaskList

    private var disposable:Disposable? = null

    init {
        disposable = tasksListRepository.findTasksList(preferencesDataStore.selectedTaskList).subscribe {
            _currentTaskList.postValue(it)
        }
    }

    fun deleteTaskList(taskList: TaskList) {
        tasksListRepository.deleteTaskList(taskList)
    }

    var listOrder
        get() = preferencesDataStore.listOrder
        set(value) { preferencesDataStore.listOrder = value }

    override fun onCleared() {
        disposable?.dispose()

        super.onCleared()
    }
}