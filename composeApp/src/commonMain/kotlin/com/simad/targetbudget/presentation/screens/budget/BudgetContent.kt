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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
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
import com.simad.targetbudget.presentation.helpers.balanceToString
import com.simad.targetbudget.presentation.helpers.plus
import com.simad.targetbudget.presentation.screens.budget.BudgetComponent.State.RealStorageSection

@Composable
fun BudgetContent(
    component: BudgetComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(vertical = BudgetSectionConstants.verticalSpacing),
    ) {
        realStoragesSection(
            state = state.realStorageSection,
        )
        item {
            Spacer(Modifier.height(BudgetSectionConstants.verticalSpacing))
        }
        realStoragesSection(
            state = state.realStorageSection,
        )
        item {
            Spacer(Modifier.height(BudgetSectionConstants.verticalSpacing))
        }
        realStoragesSection(
            state = state.realStorageSection,
        )
    }
}

fun LazyListScope.realStoragesSection(
    state: RealStorageSection
) {
    item {
        Box(
            modifier = SectionModifier(
                type = BudgetSectionClipType.FIRST,
            ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "Реальные счета / ${state.balances}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = BudgetSectionConstants.onBackgroundColor
            )
        }
    }

    state.groups.forEachIndexed { index, model ->
        val groupTitle = "${model.accessibility.name} / ${model.balances.balanceToString()}"
        groupedItems(
            title = groupTitle,
            items = model.storages,
            itemContent = { realStorage ->
                BudgetItem(
                    imageVector = Icons.Outlined.Home,
                    title = realStorage.name,
                    balance = realStorage.balanceToString(),
                    modifier = SectionModifier(
                        type = if (index == state.groups.size - 1) BudgetSectionClipType.LAST else BudgetSectionClipType.MIDDLE,
                        onClick = {
                            //TODO()
                        }
                    ),
                )
            }
        )
    }
}

fun <T> LazyListScope.groupedItems(
    title: String?,
    items: List<T>,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
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

    items(items) { item ->
        itemContent(item)
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
    val horizontalPadding = 16.dp
    val verticalSpacing = 12.dp
    val sectionCornerRadius = 28.dp
    val itemHeight = 52.dp
    val edgePadding = sectionCornerRadius - horizontalPadding
    val rowSpacing = horizontalPadding
    val cornerShape = RoundedCornerShape(sectionCornerRadius)
    val topCornerShape = RoundedCornerShape(topStart = sectionCornerRadius, topEnd = sectionCornerRadius)
    val bottomCornerShape = RoundedCornerShape(bottomStart = sectionCornerRadius, bottomEnd = sectionCornerRadius)
}

enum class BudgetSectionClipType {
    FIRST,
    MIDDLE,
    LAST
}

@Composable
fun LazyItemScope.SectionModifier(
    type: BudgetSectionClipType,
    onClick: (() -> Unit)? = null
): Modifier = with(BudgetSectionConstants) {
    when (type) {
        BudgetSectionClipType.FIRST -> Modifier
            .animateItem()
            .fillMaxSize()
            .clip(topCornerShape)
            .background(backgroundColor)
            .padding(top = edgePadding)
            .height(height = itemHeight)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalPadding)

        BudgetSectionClipType.MIDDLE -> Modifier
            .animateItem()
            .fillMaxSize()
            .height(height = itemHeight)
            .background(backgroundColor)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalPadding)

        BudgetSectionClipType.LAST -> Modifier
            .animateItem()
            .fillMaxSize()
            .clip(bottomCornerShape)
            .background(backgroundColor)
            .padding(bottom = edgePadding)
            .height(height = itemHeight)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(horizontal = horizontalPadding)
    }
}
