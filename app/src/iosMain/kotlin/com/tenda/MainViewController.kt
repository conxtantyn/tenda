package com.tenda

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.tenda.ui.core.extension.scopeOf
import com.tenda.ui.main.Main
import com.tenda.ui.main.MainScreen
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

interface TendaApplication

fun MainViewController() = ComposeUIViewController {
    val koin = startKoin {}
    val component = remember {
        Main.Builder(
            scope = koin.koin.scopeOf(named<TendaApplication>()),
        ).build()
    }
    MainScreen(component.id).Content()
}
