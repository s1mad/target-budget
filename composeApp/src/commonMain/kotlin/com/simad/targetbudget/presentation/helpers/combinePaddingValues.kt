package com.simad.targetbudget.presentation.helpers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

fun combinePaddingValues(
    first: PaddingValues,
    second: PaddingValues,
    layoutDirection: LayoutDirection
) = PaddingValues(
    start = first.calculateStartPadding(layoutDirection) + second.calculateStartPadding(layoutDirection),
    top = first.calculateTopPadding() + second.calculateTopPadding(),
    end = first.calculateEndPadding(layoutDirection) + second.calculateEndPadding(layoutDirection),
    bottom = first.calculateBottomPadding() + second.calculateBottomPadding()
)

@Composable
fun combinePaddingValues(
    first: PaddingValues,
    second: PaddingValues,
): PaddingValues {
    val layoutDirection: LayoutDirection = LocalLayoutDirection.current
    return combinePaddingValues(first, second, layoutDirection)
}

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues =
    combinePaddingValues(this, other, LayoutDirection.Ltr) // FIXME("Исправить хардкод для LayoutDirection")
