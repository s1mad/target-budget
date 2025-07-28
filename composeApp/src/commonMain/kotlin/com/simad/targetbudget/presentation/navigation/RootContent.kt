package com.simad.targetbudget.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.simad.targetbudget.presentation.screens.budget.BudgetListContent
import com.simad.targetbudget.presentation.screens.upsert_debt.UpsertDebtContent
import com.simad.targetbudget.presentation.screens.upsert_real_storage.UpsertRealStorageContent
import com.simad.targetbudget.presentation.screens.upsert_virtual_storage.UpsertVirtualStorageContent
import com.simad.targetbudget.presentation.theme.AppTheme

@Composable
fun RootContent(
    component: RootComponent
) = AppTheme {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Children(
            stack = component.stack,
            animation = stackAnimation(slide()),
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Budget -> BudgetListContent(component = child.component)
                is RootComponent.Child.UpsertRealStorage -> UpsertRealStorageContent(component = child.component)
                is RootComponent.Child.UpsertVirtualStorage -> UpsertVirtualStorageContent(component = child.component)
                is RootComponent.Child.UpsertDebt -> UpsertDebtContent(component = child.component)
            }
        }
    }
}