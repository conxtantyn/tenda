package com.tenda.feature.contact.ui.contacts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.tenda.feature.contact.ui.model.UiContact
import com.tenda.ui.core.design.DesignScene
import com.tenda.ui.core.design.DesignSceneState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ContactsPage(
    state: State<DesignSceneState<ImmutableList<UiContact>>>,
    listState: LazyListState
) {
    DesignScene(state) {
        LazyColumn(state = listState) {
            items(it.value.size) { index ->
                val contact = it.value[index]
                Text(contact.name)
            }
        }
    }
}
