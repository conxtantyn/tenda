package com.tenda.feature.contact.ui.draft

interface DraftEvent {
    fun onDraft(event: Event)

    sealed interface Event {
        data object Dismiss : Event
        data object Success : Event
    }
}
