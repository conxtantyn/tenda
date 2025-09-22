package com.tenda.feature.contact.ui.contacts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
