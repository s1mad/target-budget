package com.simad.targetbudget.di

import com.simad.targetbudget.data.di.DataModules
import com.simad.targetbudget.presentation.di.PresentationModules
import org.kodein.di.DI
import org.kodein.di.LazyDI
import org.kodein.di.direct
import org.kodein.di.factory
import org.kodein.di.instance

object DiProvider {
    var lazyDI: LazyDI? = null

    fun init(builder: DI.Builder.() -> Unit = {}) {
        val lazyDI by DI.lazy {
            builder()
            importAll(DataModules + PresentationModules)
        }
        this.lazyDI = lazyDI
    }

    inline fun <reified T : Any> inject(tag: Any? = null): T =
        lazyDI?.direct?.instance<T>(tag = tag) ?: throw RuntimeException("DiProvider not init")

    inline fun <reified A : Any, reified T : Any> factory(tag: Any? = null): (A) -> T =
        lazyDI?.direct?.factory<A, T>(tag = tag) ?: throw RuntimeException("DiProvider not init")
}

inline fun <reified T : Any> inject(tag: Any? = null): T = DiProvider.inject<T>(tag = tag)
inline fun <reified A : Any, reified T : Any> factory(tag: Any? = null): (A) -> T =
    DiProvider.factory<A, T>(tag = tag)