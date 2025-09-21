package com.tenda.feature.contact.ui.draft

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tenda.feature.contact.domain.usecase.CreateDraftUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DraftViewModel(
    private val usecase: CreateDraftUsecase
) : ScreenModel {
    private val scope = this.screenModelScope

    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun create(name: String) {
        scope.launch {
            try {
                _state.value = State.Loading
                usecase(name)
                _state.value = State.Success
            } catch (error: Throwable) {
                _state.emit(State.Error(error))
            }
        }
    }

    fun reset() {
        scope.launch {
            _state.value = State.Default
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
        data object Success : State

        data class Error(val error: Throwable) : State
    }
}
