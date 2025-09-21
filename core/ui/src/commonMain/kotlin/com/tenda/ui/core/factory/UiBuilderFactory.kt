package com.tenda.ui.core.factory

import com.tenda.ui.core.component.UiComponent
import com.tenda.ui.core.component.UiComponentProvider
import com.tenda.ui.core.exception.UiBuilderException
import kotlin.reflect.KClass

class UiBuilderFactory(private val builders: List<UiComponent.Builder>) : UiComponentProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : UiComponent.Builder> builder(clazz: KClass<T>): T {
        return builders.find { clazz.isInstance(it) } as? T
            ?: throw UiBuilderException("No builder found for ${clazz.qualifiedName}")
    }
}
