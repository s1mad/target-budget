package com.simad.targetbudget.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.simad.targetbudget.presentation.theme.BudgetSectionConstants

fun <T> LazyListScope.budgetGroup(
    title: String?,
    items: List<T>,
    itemContent: @Composable LazyItemScope.(isLast: Boolean, T) -> Unit,
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
                    fontWeight = FontWeight.ExtraLight,
                    color = BudgetSectionConstants.onBackgroundColor,
                )
            }
        }
    }

    itemsIndexed(items) { index, item ->
        val isLast = items.lastIndex == index
        itemContent(isLast, item)
    }
}