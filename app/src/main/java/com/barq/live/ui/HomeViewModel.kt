package com.barq.live.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barq.live.data.FootballApi
import com.barq.live.data.MatchDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data class Success(val matches: List<MatchDto>) : UiState
    data class Error(val message: String) : UiState
}

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    init { load() }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                val list = FootballApi.todayMatches()
                    .sortedWith(compareByDescending<MatchDto> { it.isLive }.thenBy { it.utcDate })
                UiState.Success(list)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "وقع مشكل فالتحميل")
            }
        }
    }
}
