package com.simad.targetbudget.presentation.screens.upsert_debt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.simad.targetbudget.presentation.components.AppTextField
import com.simad.targetbudget.presentation.helpers.plus
import com.simad.targetbudget.presentation.screens.upsert_debt.UpsertDebtComponent.Event

@Composable
fun UpsertDebtContent(
    component: UpsertDebtComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            AppTextField(
                value = state.name,
                onValueChange = {
                    component.onEvent(Event.ChangeName(new = it))
                },
                modifier = Modifier.fillMaxWidth(),
                label = "Name"
            )
        }
        item {
            AppTextField(
                value = state.balance,
                onValueChange = {
                    component.onEvent(Event.ChangeBalance(new = it))
                },
                modifier = Modifier.fillMaxWidth(),
                label = "Balance (+ Owe me) (- I owe)",
                keyboardType = KeyboardType.Number
            )
        }
        item {
            Button(
                onClick = { component.onEvent(Event.Upsert) },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(text = if (state.isInsert) "Create" else "Update")
            }
        }
        if (!state.isInsert) {
            item {
                if (state.confirmDelete) {
                    Button(
                        onClick = { component.onEvent(Event.Delete) },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    ) {
                        Text(text = "Confirm Delete")
                    }
                } else {
                    OutlinedButton(
                        onClick = { component.onEvent(Event.Delete) },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                    }

                }

            }
        }
        item {
            OutlinedButton(
                onClick = { component.onEvent(Event.NavBack) },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(text = "Cancel")
            }
        }
    }
}
