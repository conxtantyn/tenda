package com.tenda.ui.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tenda.persistence.core.usecase.SynchroniseUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class HomeViewModel(
    private val usecase: SynchroniseUsecase
) : ScreenModel {
    private val scope = screenModelScope

    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun synchronize() {
        scope.launch {
            try {
                _state.value = State.Loading
                usecase()
                val timestamp = Clock.System.now().toEpochMilliseconds()
                _state.value = State.Synchronized(timestamp)
            } catch (error: Throwable) {
                _state.emit(State.Error(error))
            }
        }
    }

    fun reset() {
        scope.launch {
            _state.emit(State.Default)
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Synchronized(val timestamp: Long) : State
        data class Error(val error: Throwable) : State
    }
}
