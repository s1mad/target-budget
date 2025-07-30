package com.simad.targetbudget.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object BudgetSectionConstants {
    val backgroundColor @Composable get() = MaterialTheme.colorScheme.surface
    val onBackgroundColor @Composable get() = MaterialTheme.colorScheme.onSurface
    val horizontalOuterPadding = 0.dp
    val horizontalInnerPadding = 16.dp
    val verticalSpacing = 8.dp
    val sectionCornerRadius = 28.dp
    val itemHeight = 52.dp
    val edgePadding = sectionCornerRadius - horizontalInnerPadding
    val rowSpacing = horizontalInnerPadding
    val cornerShape = RoundedCornerShape(sectionCornerRadius)
    val topCornerShape = RoundedCornerShape(topStart = sectionCornerRadius, topEnd = sectionCornerRadius)
    val bottomCornerShape = RoundedCornerShape(bottomStart = sectionCornerRadius, bottomEnd = sectionCornerRadius)
}