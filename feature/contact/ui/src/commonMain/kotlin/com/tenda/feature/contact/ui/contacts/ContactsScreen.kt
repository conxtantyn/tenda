package com.tenda.feature.contact.ui.contacts

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.tenda.common.schema.Paging
import com.tenda.ui.core.design.DesignSceneState
import com.tenda.ui.core.extension.factory
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.koin.core.scope.ScopeID

@OptIn(ExperimentalMaterial3Api::class)
class ContactsScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val component = factory(scope).builder(Contacts.Builder::class).build()
        val viewModel = rememberScreenModel { component.get<ContactsViewModel>() }
        val state = viewModel.state.collectAsStateWithLifecycle()
        val listState = rememberLazyListState()
        val derivedState = remember {
            derivedStateOf {
                when(state.value) {
                    ContactsViewModel.State.Default -> DesignSceneState.Default
                    ContactsViewModel.State.Loading -> DesignSceneState.Loading
                    is ContactsViewModel.State.Success -> DesignSceneState.Success(
                        (state.value as ContactsViewModel.State.Success).contacts
                    )
                    is ContactsViewModel.State.Error -> DesignSceneState.Error(
                        (state.value as ContactsViewModel.State.Error).error
                    )
                }
            }
        }
        ContactsPage(
            state = derivedState,
            listState = listState
        )
        LaunchedEffect(Unit) {
            while (isActive) {
                viewModel.load(Paging.Request(
                    offset = 0,
                    limit = 100
                ))
                delay(300)
            }
        }
    }
}
