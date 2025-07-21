package com.simad.targetbudget.presentation.di

import com.arkivanov.decompose.ComponentContext
import com.simad.targetbudget.presentation.screens.budget.BudgetComponent
import com.simad.targetbudget.presentation.screens.budget.BudgetComponentImpl
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

internal val DecomposeModule = DI.Module("PresentationDecomposeModule") {
    bindFactory<ComponentContext, BudgetComponent> { componentContext: ComponentContext ->
        BudgetComponentImpl(
            componentContext = componentContext,
            realStorageRepository = instance(),
            virtualStorageRepository = instance(),
            debtRepository = instance()
        )
    }
}
