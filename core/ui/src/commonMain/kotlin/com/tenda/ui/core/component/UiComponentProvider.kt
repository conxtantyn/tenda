package com.tenda.ui.core.component

import kotlin.reflect.KClass

interface UiComponentProvider {
    fun factory(): Factory

    interface Factory {
        fun <T : UiComponent.Builder> builder(clazz: KClass<T>): T
    }
}
