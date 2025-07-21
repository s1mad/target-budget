package com.simad.targetbudget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.simad.targetbudget.presentation.screens.budget.BudgetComponent
import com.simad.targetbudget.presentation.screens.budget.BudgetContent
import com.simad.targetbudget.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App(
    budgetComponent: BudgetComponent
) = AppTheme {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        BudgetContent(
            component = budgetComponent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
