package com.example.multiplatformtest.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplatformtest.Resource
import com.example.multiplatformtest.SampleRepository
import com.example.multiplatformtest.SampleRepositoryImpl
import com.example.multiplatformtest.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val sampleRepository: SampleRepository = SampleRepositoryImpl()

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    fun getUsers() {
        viewModelScope.launch {
            sampleRepository.getUsers().collect { res ->
                _uiState.value = when (res) {
                    is Resource.Loading -> MainActivityUiState.Loading

                    is Resource.Error -> MainActivityUiState.Error(res.exception.message ?: "")

                    is Resource.Success -> MainActivityUiState.Success(res.data)
                }
            }
        }
    }

    fun addUser() {}

    fun updateUser(user: User) {}

    fun deleteUser(user: User) {}
}

sealed class MainActivityUiState {
    object Loading : MainActivityUiState()

    data class Success(val users: List<User>) : MainActivityUiState()

    data class Error(val errorMessage: String) : MainActivityUiState()
}