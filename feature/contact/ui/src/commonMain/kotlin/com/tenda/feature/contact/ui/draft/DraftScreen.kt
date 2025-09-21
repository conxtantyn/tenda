package com.tenda.feature.contact.ui.draft

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.tenda.ui.core.extension.factory
import org.koin.core.scope.ScopeID

class DraftScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val component = factory(scope).builder(Draft.Builder::class).build()
        val viewModel = rememberScreenModel { component.get<DraftViewModel>() }
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = component.getScope(scope).get<DraftEvent>()
        val name = remember { TextFieldState() }
        val isLoading = remember { derivedStateOf {
            state.value is DraftViewModel.State.Loading
        } }
        val isSuccess = remember {
            derivedStateOf {
                state.value is DraftViewModel.State.Success
            }
        }
        val error = remember { derivedStateOf {
            (state.value as? DraftViewModel.State.Error?)?.error
        } }
        val enabled = remember {
            derivedStateOf {
                name.text.toString().isNotBlank()
            }
        }
        DraftPage(
            name = name,
            isLoading = isLoading,
            enabled = enabled,
            error = error,
            onDismiss = { event.onDraft(DraftEvent.Event.Dismiss) }
        ) { viewModel.create(it) }
        LaunchedEffect(isSuccess.value) {
            if (isSuccess.value) {
                viewModel.reset()
                event.onDraft(DraftEvent.Event.Success)
            }
        }
    }
}
