package com.tenda.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.tenda.feature.contact.ui.contacts.ContactsScreen
import com.tenda.feature.contact.ui.draft.DraftScreen
import com.tenda.ui.core.extension.factory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.scope.ScopeID

@OptIn(ExperimentalMaterial3Api::class)
class HomeScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val dispatcher = rememberCoroutineScope()
        val component = factory(scope).builder(Home.Builder::class).build()
        val interactor = component.get<HomeInteractor>()
        val viewModel = rememberScreenModel { component.get<HomeViewModel>() }
        val state = viewModel.state.collectAsStateWithLifecycle()
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val isLoading = remember { derivedStateOf {
            state.value is HomeViewModel.State.Loading
        } }
        val isInitialized = remember {
            derivedStateOf {
                state.value is HomeViewModel.State.Synchronized
            }
        }
        val error = remember { derivedStateOf {
            (state.value as? HomeViewModel.State.Error?)?.error
        } }
        LaunchedEffect(sheetState.isVisible) {
            interactor.attach(
                scope = component,
                args = {
                    dispatcher.launch {
                        sheetState.hide()
                    }
                }
            )
        }
        HomePage(
            isLoading = isLoading,
            error = error,
            sheetState = sheetState,
            onSynchronised = {
                viewModel.synchronize()
            },
            draft = {
                DraftScreen(component.id).Content()
            }
        ) { ContactsScreen(component.id).Content() }
        LaunchedEffect(isInitialized.value) {
            if (isInitialized.value) {
                viewModel.reset()
            }
        }
        LaunchedEffect(error.value) {
            if (error.value != null) {
                delay(1000)
                viewModel.reset()
            }
        }
    }
}
