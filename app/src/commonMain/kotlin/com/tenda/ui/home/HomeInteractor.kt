package com.tenda.ui.home

import com.tenda.feature.contact.ui.draft.DraftEvent
import com.tenda.ui.core.component.UiComponent

class HomeInteractor : UiComponent.ScreenInteractorWithArgs<() -> Unit>(), DraftEvent {
    override fun onDraft(event: DraftEvent.Event) {
        args()
    }
}
