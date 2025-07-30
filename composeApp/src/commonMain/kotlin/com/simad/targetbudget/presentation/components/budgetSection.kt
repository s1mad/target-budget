package com.simad.targetbudget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simad.targetbudget.presentation.theme.BudgetSectionConstants

data class SectionGroup<T>(
    val title: String?,
    val items: List<T>
)

fun <T> LazyListScope.budgetSection(
    title: String,
    groups: List<SectionGroup<T>>,
    itemContent: @Composable LazyItemScope.(isLast: Boolean, T) -> Unit,
    onInsert: (() -> Unit)? = null,
) {
    item {
        Row(
            modifier = SectionModifier(
                type = if (groups.isEmpty()) BudgetSectionClipType.EMPTY else BudgetSectionClipType.FIRST,
                onClick = onInsert
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = BudgetSectionConstants.onBackgroundColor
            )
            onInsert?.let {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

    groups.forEachIndexed { groupIndex, group ->
        budgetGroup(
            title = group.title,
            items = group.items,
            itemContent = { itemIsLast, item ->
                val isLast = itemIsLast && groups.lastIndex == groupIndex
                itemContent(isLast, item)
            }
        )
    }
}