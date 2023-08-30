package com.example.multiplatformtest.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WordleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WordleState())
    val uiState = _uiState.asStateFlow()

    fun onLetterClicked(letter: Char) {
        _uiState.update {
            it.copy(
                // Update uiState
            )
        }
    }

    fun onSubmitClick() {}

    fun onDeleteClick() {}

    data class WordleState(
        val guesses: List<List<AppState.Letter>> = emptyList()
    )
}