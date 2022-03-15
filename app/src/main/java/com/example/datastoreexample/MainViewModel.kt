package com.example.datastoreexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val datastoreRepository: DatastoreRepository) : ViewModel() {

    private val _preferenceCount = MutableStateFlow("")
    val preferenceCount = _preferenceCount.asStateFlow()

    private val _protoCount = MutableStateFlow("")
    val protoCount = _protoCount.asStateFlow()

    private var preferencesClickCount = 0
    private var protoClickCount = 0

    init {
        getUserPreferences()
        getUserProto()
    }

    fun onPreferencesClickCount() {
        updateUserPreferences()
    }

    fun onProtoClickCount() {
        updateUserProto()
    }

    private fun getUserPreferences() {
        viewModelScope.launch {
            datastoreRepository.getUserPreferences().collect {
                preferencesClickCount = it.clickCount
                _preferenceCount.value = "${it.clickCount}"
            }
        }
    }

    private fun updateUserPreferences() {
        viewModelScope.launch {
            preferencesClickCount++
            datastoreRepository.updatePreferencesClickCount(preferencesClickCount)
        }
    }

    private fun getUserProto() {
        viewModelScope.launch {
            datastoreRepository.getUserProto().collect {
                protoClickCount = it.clickCount
                _protoCount.value = "${it.clickCount}"
            }
        }
    }

    private fun updateUserProto() {
        viewModelScope.launch {
            protoClickCount++
            datastoreRepository.updateUserProto(protoClickCount)
        }
    }
}

class MainViewModelFactory(
    private val datastoreRepository: DatastoreRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(datastoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}