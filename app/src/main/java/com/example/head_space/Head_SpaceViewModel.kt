package com.example.head_space

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Head_SpaceViewModel: ViewModel() {
        private val _uiState = MutableStateFlow(Head_SpaceUiState())
        val uiState: StateFlow<Head_SpaceUiState> = _uiState.asStateFlow()

    fun setUser(user: String) {
        _uiState.update {
            it.copy(user = user)
        }
    }

    fun setThread(thread: String) {
        _uiState.update {
            it.copy(thread = thread)
        }
    }

    fun openOrCloseCreateNewThreadDialog() {
        _uiState.update {
            it.copy(openCreateNewThreadDialog = !it.openCreateNewThreadDialog)
        }
    }

}

data class Head_SpaceUiState(
    val user: String = "",
    val users: List<String> = listOf("bob","smol bob","fiona","puppy"),
    val thread: String = "",
    val openCreateNewThreadDialog: Boolean = false
)