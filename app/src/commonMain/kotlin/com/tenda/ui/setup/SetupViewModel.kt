package com.tenda.ui.setup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tenda.usecase.InitializeUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetupViewModel(
    private val usecase: InitializeUsecase
) : ScreenModel {
    private val scope = screenModelScope

    private val _state = MutableStateFlow<State>(State.Default)

    val state: StateFlow<State> = _state.asStateFlow()

    fun initialize(
        url: String,
        token: String,
        database: String
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                _state.value = State.Loading
                usecase(
                    InitializeUsecase.Argument(
                        url = url,
                        token = token,
                        database = database
                    )
                )
                _state.value = State.Initialized
            } catch (error: Throwable) {
                _state.emit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Default : State
        data object Loading : State
        data object Initialized : State

        data class Error(val error: Throwable) : State
    }
}
