package com.tenda.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import com.tenda.feature.contact.ui.contacts.ContactsScreen
import com.tenda.feature.contact.ui.draft.DraftScreen
import com.tenda.ui.core.extension.factory
import kotlinx.coroutines.launch
import org.koin.core.scope.ScopeID

@OptIn(ExperimentalMaterial3Api::class)
class HomeScreen(val scope: ScopeID) : Screen {
    @Composable
    override fun Content() {
        val dispatcher = rememberCoroutineScope()
        val component = factory(scope).builder(Home.Builder::class).build()
        val interactor = component.get<HomeInteractor>()
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        LaunchedEffect(Unit) {
            interactor.attach(
                scope = component,
                args = {
                    dispatcher.launch {
                        sheetState.hide()
                    }
                }
            )
        }
        HomePage(sheetState = sheetState, {
            DraftScreen(component.id).Content()
        }) {
            ContactsScreen(component.id).Content()
        }
    }
}
