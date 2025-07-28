package com.simad.targetbudget.presentation.screens.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simad.targetbudget.presentation.helpers.balanceEasyRead
import com.simad.targetbudget.presentation.helpers.balancesEasyRead
import com.simad.targetbudget.presentation.helpers.bufferEasyRead
import com.simad.targetbudget.presentation.helpers.negativeBalancesEasyRead
import com.simad.targetbudget.presentation.helpers.plus
import com.simad.targetbudget.presentation.helpers.positiveBalancesEasyRead
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.Event
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.DebtSection
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.RealStorageSection
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State.VirtualStorageSection

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
    item {
        Row(
            modifier = SectionModifier(
                type = if (state.groups.isEmpty()) BudgetSectionClipType.EMPTY else BudgetSectionClipType.FIRST,
                onClick = { onEvent(Event.InsertRealStorage) }
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Real Storages / ${state.balancesEasyRead()}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = BudgetSectionConstants.onBackgroundColor
            )
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }

    state.groups.forEachIndexed { groupIndex, model ->
        val groupTitle = "${model.accessibility.name} / ${model.balancesEasyRead()}"
        groupedItems(
            title = groupTitle,
            items = model.storages,
            itemContent = { itemsIndex, storage ->
                BudgetItem(
                    imageVector = Icons.Outlined.Home,
                    title = storage.name,
                    balance = storage.balanceEasyRead(),
                    modifier = SectionModifier(
                        type = if (groupIndex == state.groups.lastIndex && itemsIndex == model.storages.lastIndex) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                        onClick = { onEvent(Event.UpdateRealStorage(storage)) }
                    ),
                )
            }
        )
    }
}

fun LazyListScope.virtualStoragesSection(
    state: VirtualStorageSection,
    onEvent: (Event) -> Unit
) {
    item {
        Row(
            modifier = SectionModifier(
                type = if (state.storages.isEmpty()) BudgetSectionClipType.EMPTY else BudgetSectionClipType.FIRST,
                onClick = { onEvent(Event.InsertVirtualStorage) }
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Virtual Storages / ${state.balancesEasyRead()}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = BudgetSectionConstants.onBackgroundColor
            )
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }

    val bufferTitle = "Buffer / ${state.bufferEasyRead()}"
    groupedItems(
        title = bufferTitle,
        items = state.storages,
        itemContent = { itemsIndex, storage ->
            BudgetItem(
                imageVector = Icons.Outlined.Home,
                title = storage.name,
                balance = storage.balanceEasyRead(),
                modifier = SectionModifier(
                    type = if (itemsIndex == state.storages.lastIndex) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                    onClick = { onEvent(Event.UpdateVirtualStorage(storage)) }
                ),
            )
        }
    )
}

fun LazyListScope.debtsSection(
    state: DebtSection,
    onEvent: (Event) -> Unit
) {
    item {
        Row(
            modifier = SectionModifier(
                type = if (state.debts.isEmpty()) BudgetSectionClipType.EMPTY else BudgetSectionClipType.FIRST,
                onClick = { onEvent(Event.InsertDebt) }
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Debts / ${state.positiveBalancesEasyRead()} / ${state.negativeBalancesEasyRead()}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = BudgetSectionConstants.onBackgroundColor
            )
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .size(28.dp)
            )
        }
    }

    groupedItems(
        title = null,
        items = state.debts,
        itemContent = { itemsIndex, debt ->
            BudgetItem(
                imageVector = Icons.Outlined.Home,
                title = debt.name,
                balance = debt.balanceEasyRead(),
                modifier = SectionModifier(
                    type = if (itemsIndex == state.debts.lastIndex) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                    onClick = { onEvent(Event.UpdateDebt(debt)) }
                ),
            )
        }
    )
}

fun <T> LazyListScope.groupedItems(
    title: String?,
    items: List<T>,
    itemContent: @Composable LazyItemScope.(Int, T) -> Unit,
) {
    if (title != null && items.isNotEmpty()) {
        item {
            Box(
                modifier = SectionModifier(type = BudgetSectionClipType.MIDDLE),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = BudgetSectionConstants.onBackgroundColor.copy(alpha = 0.6f),
                )
            }
        }
    }

    itemsIndexed(items) { index, item ->
        itemContent(index, item)
    }
}

@Composable
fun BudgetItem(
    imageVector: ImageVector,
    title: String,
    balance: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(BudgetSectionConstants.rowSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Item icon",
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal

        )
        Text(
            text = balance,
            modifier = Modifier,
            fontSize = 17.sp,
        )
    }
}

object BudgetSectionConstants {
    val backgroundColor @Composable get() = MaterialTheme.colorScheme.surface
    val onBackgroundColor @Composable get() = MaterialTheme.colorScheme.onSurface
    val horizontalOuterPadding = 8.dp
    val horizontalInnerPadding = 16.dp
    val verticalSpacing = 12.dp
    val sectionCornerRadius = 28.dp
    val itemHeight = 52.dp
    val edgePadding = sectionCornerRadius - horizontalInnerPadding
    val rowSpacing = horizontalInnerPadding
    val cornerShape = RoundedCornerShape(sectionCornerRadius)
    val topCornerShape = RoundedCornerShape(topStart = sectionCornerRadius, topEnd = sectionCornerRadius)
    val bottomCornerShape = RoundedCornerShape(bottomStart = sectionCornerRadius, bottomEnd = sectionCornerRadius)
}

enum class BudgetSectionClipType {
    FIRST,
    MIDDLE,
    LAST,
    EMPTY
}

@Composable
fun LazyItemScope.SectionModifier(
    type: BudgetSectionClipType,
    onClick: (() -> Unit)? = null
): Modifier = with(BudgetSectionConstants) {
    val baseModifier = Modifier
        .animateItem()
        .fillMaxSize()
        .padding(horizontal = horizontalOuterPadding)

    return@with when (type) {
        BudgetSectionClipType.FIRST -> baseModifier
            .clip(topCornerShape)
            .background(backgroundColor)
            .padding(top = edgePadding)
            .height(height = itemHeight)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalInnerPadding)

        BudgetSectionClipType.MIDDLE -> baseModifier
            .height(height = itemHeight)
            .background(backgroundColor)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalInnerPadding)

        BudgetSectionClipType.LAST -> baseModifier
            .clip(bottomCornerShape)
            .background(backgroundColor)
            .padding(bottom = edgePadding)
            .height(height = itemHeight)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalInnerPadding)

        BudgetSectionClipType.EMPTY -> baseModifier
            .clip(cornerShape)
            .background(backgroundColor)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalInnerPadding, vertical = edgePadding)
            .height(height = itemHeight)
    }
}