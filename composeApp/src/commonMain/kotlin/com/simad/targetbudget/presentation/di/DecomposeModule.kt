package com.simad.targetbudget.presentation.di

import com.simad.targetbudget.presentation.navigation.RootComponent
import com.simad.targetbudget.presentation.navigation.RootComponentImpl
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_debt.InsertDebtComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_debt.UpdateDebtComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_debt.UpsertDebtComponent
import com.simad.targetbudget.presentation.screens.upsert_real_storage.InsertRealStorageComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_real_storage.UpdateRealStorageComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_real_storage.UpsertRealStorageComponent
import com.simad.targetbudget.presentation.screens.upsert_virtual_storage.InsertVirtualStorageComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_virtual_storage.UpdateVirtualStorageComponentImpl
import com.simad.targetbudget.presentation.screens.upsert_virtual_storage.UpsertVirtualStorageComponent
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

internal val DecomposeModule = DI.Module("PresentationDecomposeModule") {
    bindFactory<RootComponent.FactoryParams, RootComponent> { params ->
        RootComponentImpl(
            componentContext = params.componentContext,
        )
    }

    bindFactory<BudgetListComponent.FactoryParams, BudgetListComponent> { params ->
        BudgetListComponentImpl(
            realStorageRepository = instance(),
            virtualStorageRepository = instance(),
            debtRepository = instance(),
            componentContext = params.componentContext,
            navToUpsertRealStorage = params.navToUpsertRealStorage,
            navToUpsertVirtualStorage = params.navToUpsertVirtualStorage,
            navToUpsertDebt = params.navToUpsertDebt
        )
    }

    bindFactory<UpsertRealStorageComponent.FactoryParams, UpsertRealStorageComponent> { params ->
        if (params.model != null) {
            UpdateRealStorageComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
                model = params.model
            )
        } else {
            InsertRealStorageComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
            )
        }
    }

    bindFactory<UpsertVirtualStorageComponent.FactoryParams, UpsertVirtualStorageComponent> { params ->
        if (params.model != null) {
            UpdateVirtualStorageComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
                model = params.model
            )
        } else {
            InsertVirtualStorageComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
            )
        }
    }

    bindFactory<UpsertDebtComponent.FactoryParams, UpsertDebtComponent> { params ->
        if (params.model != null) {
            UpdateDebtComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
                model = params.model
            )
        } else {
            InsertDebtComponentImpl(
                repo = instance(),
                componentContext = params.componentContext,
                navBack = params.navBack,
            )
        }
    }
}
