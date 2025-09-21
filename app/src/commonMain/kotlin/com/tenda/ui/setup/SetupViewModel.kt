package com.tenda.ui.setup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tenda.usecase.PersistenceInitUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetupViewModel(
    private val usecase: PersistenceInitUsecase
) : ScreenModel {
    private val scope = screenModelScope

    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun initialize() {
        scope.launch {
            _state.value = State.Loading
            usecase()
            _state.value = State.Default
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
    }
}
