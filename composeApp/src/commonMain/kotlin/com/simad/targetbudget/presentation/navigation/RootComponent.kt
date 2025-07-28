package com.simad.targetbudget.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.simad.targetbudget.di.factory
import com.simad.targetbudget.domain.model.DebtModel
import com.simad.targetbudget.domain.model.RealStorageModel
import com.simad.targetbudget.domain.model.VirtualStorageModel
import com.simad.targetbudget.presentation.navigation.RootComponent.Child
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent
import com.simad.targetbudget.presentation.screens.upsert_debt.UpsertDebtComponent
import com.simad.targetbudget.presentation.screens.upsert_real_storage.UpsertRealStorageComponent
import com.simad.targetbudget.presentation.screens.upsert_virtual_storage.UpsertVirtualStorageComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Budget(val component: BudgetListComponent) : Child
        class UpsertRealStorage(val component: UpsertRealStorageComponent) : Child
        class UpsertVirtualStorage(val component: UpsertVirtualStorageComponent) : Child
        class UpsertDebt(val component: UpsertDebtComponent) : Child
    }

    data class FactoryParams(
        val componentContext: ComponentContext,
    )
}

class RootComponentImpl(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private fun navBack() = navigation.pop()
    private fun navToUpsertRealStorage(model: RealStorageModel?) =
        navigation.bringToFront(configuration = Config.UpsertRealStorage(model))

    private fun navToUpsertVirtualStorage(model: VirtualStorageModel?) =
        navigation.bringToFront(configuration = Config.UpsertVirtualStorage(model))

    private fun navToUpsertDebt(model: DebtModel?) =
        navigation.bringToFront(configuration = Config.UpsertDebt(model))


    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Budget,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Budget -> Child.Budget(
                component = factory<BudgetListComponent.FactoryParams, BudgetListComponent>().invoke(
                    BudgetListComponent.FactoryParams(
                        componentContext = componentContext,
                        navToUpsertRealStorage = ::navToUpsertRealStorage,
                        navToUpsertVirtualStorage = ::navToUpsertVirtualStorage,
                        navToUpsertDebt = ::navToUpsertDebt
                    )
                )
            )

            is Config.UpsertRealStorage -> Child.UpsertRealStorage(
                component = factory<UpsertRealStorageComponent.FactoryParams, UpsertRealStorageComponent>().invoke(
                    UpsertRealStorageComponent.FactoryParams(
                        componentContext = componentContext,
                        navBack = ::navBack,
                        model = config.model
                    )
                )
            )

            is Config.UpsertVirtualStorage -> Child.UpsertVirtualStorage(
                component = factory<UpsertVirtualStorageComponent.FactoryParams, UpsertVirtualStorageComponent>().invoke(
                    UpsertVirtualStorageComponent.FactoryParams(
                        componentContext = componentContext,
                        navBack = ::navBack,
                        model = config.model
                    )
                )
            )

            is Config.UpsertDebt -> Child.UpsertDebt(
                component = factory<UpsertDebtComponent.FactoryParams, UpsertDebtComponent>().invoke(
                    UpsertDebtComponent.FactoryParams(
                        componentContext = componentContext,
                        navBack = ::navBack,
                        model = config.model
                    )
                )
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Budget : Config

        @Serializable
        data class UpsertRealStorage(val model: RealStorageModel?) : Config

        @Serializable
        data class UpsertVirtualStorage(val model: VirtualStorageModel?) : Config

        @Serializable
        data class UpsertDebt(val model: DebtModel?) : Config
    }
}