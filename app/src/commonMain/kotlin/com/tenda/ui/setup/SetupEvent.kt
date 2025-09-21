package com.tenda.ui.setup

interface SetupEvent {
    fun onSetup(event: Event)

    sealed interface Event {
        data object Initialized : Event
    }
}
