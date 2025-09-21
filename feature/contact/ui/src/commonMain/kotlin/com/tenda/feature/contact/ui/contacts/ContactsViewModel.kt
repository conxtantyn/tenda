package com.tenda.feature.contact.ui.contacts

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tenda.common.schema.Paging
import com.tenda.feature.contact.domain.usecase.ContactListUsecase
import com.tenda.feature.contact.ui.mapper.mapFromDomain
import com.tenda.feature.contact.ui.model.UiContact
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val usecase: ContactListUsecase
) : ScreenModel {
    private val scope = this.screenModelScope

    private val _status = MutableStateFlow<Status>(Status.Default)

    private val _state = MutableStateFlow<State>(State.Default)

    val status: StateFlow<Status> = _status.asStateFlow()

    val state: StateFlow<State> = _state.asStateFlow()

    fun load(paging: Paging.Request) {
        scope.launch {
            if (_state.value is State.Success) {
                _status.emit(Status.Processing)
            } else {
                _state.emit(State.Loading)
            }
            try {
                val response = usecase(paging)
                _state.value = State.Success(response.items.map {
                    it.mapFromDomain()
                }.toImmutableList())
            } catch (error: Throwable) {
                if (_state.value is State.Success) {
                    _status.emit(Status.Error(error))
                } else {
                    _state.emit(State.Error(error))
                }
            }
        }
    }

    sealed interface Status {
        data object Default : Status
        data object Processing : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Success(val contacts: ImmutableList<UiContact>) : State
        data class Error(val error: Throwable) : State
    }
}
