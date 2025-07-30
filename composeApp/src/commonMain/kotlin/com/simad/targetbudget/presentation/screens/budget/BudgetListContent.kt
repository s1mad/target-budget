package com.simad.targetbudget.presentation.screens.budget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.simad.targetbudget.presentation.components.BudgetItem
import com.simad.targetbudget.presentation.components.BudgetSectionClipType
import com.simad.targetbudget.presentation.components.SectionGroup
import com.simad.targetbudget.presentation.components.SectionModifier
import com.simad.targetbudget.presentation.components.budgetSection
import com.simad.targetbudget.presentation.helpers.balanceEasyRead
import com.simad.targetbudget.presentation.helpers.balancesEasyRead
import com.simad.targetbudget.presentation.helpers.bufferEasyRead
import com.simad.targetbudget.presentation.helpers.moneyLongToEasyReadString
import com.simad.targetbudget.presentation.helpers.oweIBalancesEasyRead
import com.simad.targetbudget.presentation.helpers.oweMeBalancesEasyRead
import com.simad.targetbudget.presentation.helpers.plus
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.Event
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.DebtSection
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.RealStorageSection
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.VirtualStorageSection
import com.simad.targetbudget.presentation.theme.BudgetSectionConstants

@Composable
fun BudgetListContent(
    component: BudgetListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(vertical = BudgetSectionConstants.verticalSpacing),
    ) {
        realStoragesSection(
            state = state.realStorageSection,
            onEvent = component::onEvent
        )
        item {
            Spacer(Modifier.height(BudgetSectionConstants.verticalSpacing))
        }
        virtualStoragesSection(
            state = state.virtualStorageSection,
            onEvent = component::onEvent
        )
        item {
            Spacer(Modifier.height(BudgetSectionConstants.verticalSpacing))
        }
        debtsSection(
            state = state.debtSection,
            onEvent = component::onEvent
        )
    }
}

fun LazyListScope.realStoragesSection(
    state: RealStorageSection,
    onEvent: (Event) -> Unit
) {
    budgetSection(
        title = "Real Storages / ${moneyLongToEasyReadString(state.balances + state.creditLimits)}",
        groups = state.groups.map { model ->
            SectionGroup(
                title = "${model.accessibility.name} / ${moneyLongToEasyReadString(model.balances + model.creditLimits)}",
                items = model.storages
            )
        },
        itemContent = { isLast, item ->
            BudgetItem(
                imageVector = Icons.Rounded.Home,
                title = item.name,
                balance = item.balanceEasyRead(),
                subtitle = if (item.creditLimit > 0) "Available credit ${moneyLongToEasyReadString(item.balance + item.creditLimit)} ₽" else null,
                modifier = SectionModifier(
                    type = if (isLast) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                    onClick = { onEvent(Event.UpdateRealStorage(item)) }
                ),
            )
        },
        onInsert = { onEvent(Event.InsertRealStorage) },
    )
}

fun LazyListScope.virtualStoragesSection(
    state: VirtualStorageSection,
    onEvent: (Event) -> Unit
) {
    budgetSection(
        title = "Virtual Storages / ${state.balancesEasyRead()}",
        groups = listOf(
            SectionGroup(
                title = "Buffer / ${state.bufferEasyRead()}",
                items = state.storages
            )
        ),
        itemContent = { isLast, item ->
            BudgetItem(
                imageVector = Icons.Rounded.Favorite,
                title = item.name,
                balance = item.balanceEasyRead(),
                modifier = SectionModifier(
                    type = if (isLast) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                    onClick = { onEvent(Event.UpdateVirtualStorage(item)) }
                ),
                subtitle = item.description
            )
        },
        onInsert = { onEvent(Event.InsertVirtualStorage) },
    )
}

fun LazyListScope.debtsSection(
    state: DebtSection,
    onEvent: (Event) -> Unit
) {
    budgetSection(
        title = "Debts / ${state.oweMeBalancesEasyRead()} / ${state.oweIBalancesEasyRead()}",
        groups = listOfNotNull(
            state.oweMe.debts.takeIf { list -> list.isNotEmpty() }?.let {
                SectionGroup(
                    title = "Owe me",
                    items = state.oweMe.debts
                )
            },
            state.oweI.debts.takeIf { list -> list.isNotEmpty() }?.let {
                SectionGroup(
                    title = "I owe",
                    items = state.oweI.debts
                )
            }
        ),
        itemContent = { isLast, item ->
            BudgetItem(
                imageVector = Icons.Rounded.Person,
                title = item.name,
                balance = item.balanceEasyRead(),
                modifier = SectionModifier(
                    type = if (isLast) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                    onClick = { onEvent(Event.UpdateDebt(item)) }
                ),
            )
        },
        onInsert = { onEvent(Event.InsertDebt) },
    )
}