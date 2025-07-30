package com.simad.targetbudget.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.simad.targetbudget.presentation.theme.BudgetSectionConstants

enum class BudgetSectionClipType {
    FIRST,
    MIDDLE,
    LAST,
    EMPTY
}

@SuppressLint("ComposableNaming", "ModifierFactoryExtensionFunction")
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