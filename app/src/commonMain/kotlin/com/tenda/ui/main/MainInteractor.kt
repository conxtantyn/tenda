package com.tenda.ui.main

import com.tenda.ui.core.component.UiComponent
import com.tenda.ui.home.HomeScreen
import com.tenda.ui.setup.SetupEvent

class MainInteractor : UiComponent.ScreenInteractor(), SetupEvent {
    override fun onSetup(event: SetupEvent.Event) {
        navigator.replaceAll(HomeScreen(scope.id))
    }
}
