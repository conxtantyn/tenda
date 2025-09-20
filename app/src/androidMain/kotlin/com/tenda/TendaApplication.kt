package com.tenda

import android.app.Application
import com.tenda.ui.core.extension.scopeOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class TendaApplication : Application() {
    lateinit var scope: Scope

    override fun onCreate() {
        super.onCreate()
        scope = startKoin {}.koin.scopeOf(named<TendaApplication>())
    }
}
