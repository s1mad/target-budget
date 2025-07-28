package com.simad.targetbudget.presentation.screens.upsert_debt

import com.arkivanov.decompose.ComponentContext
import com.simad.targetbudget.domain.model.DebtInsertModel
import com.simad.targetbudget.domain.model.DebtModel
import com.simad.targetbudget.domain.repository.DebtRepository
import com.simad.targetbudget.presentation.helpers.componentCoroutineScope
import com.simad.targetbudget.presentation.helpers.isValidMoneyStringInput
import com.simad.targetbudget.presentation.helpers.moneyLongToString
import com.simad.targetbudget.presentation.helpers.moneyStringToLong
import com.simad.targetbudget.presentation.screens.upsert_debt.UpsertDebtComponent.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UpsertDebtComponent {
    val state: StateFlow<State>
    fun onEvent(event: Event)

    data class State(
        val name: String = "",
        val balance: String = "",
        val isInsert: Boolean = true,
        val confirmDelete: Boolean = false,
    )

    sealed interface Event {
        data class ChangeName(val new: String) : Event
        data class ChangeBalance(val new: String) : Event
        data object Upsert : Event
        data object Delete : Event
        data object NavBack : Event
    }

    data class FactoryParams(
        val componentContext: ComponentContext,
        val navBack: () -> Unit,
        val model: DebtModel? = null
    )
}

abstract class UpsertDebtComponentAbstract(
    componentContext: ComponentContext,
    private val navBack: () -> Unit
) : UpsertDebtComponent, ComponentContext by componentContext {
    override val state = MutableStateFlow(UpsertDebtComponent.State())
    protected val scope = componentCoroutineScope()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ChangeName -> state.update { current -> current.copy(name = event.new) }
            is Event.ChangeBalance -> if (isValidMoneyStringInput(event.new) || event.new.isEmpty()) {
                state.update { current -> current.copy(balance = event.new) }
            }

            Event.Upsert -> if (state.value.name.isNotBlank()) upsert()
            Event.Delete -> delete()
            Event.NavBack -> navBack()
        }
    }

    abstract fun upsert()
    abstract fun delete()
}

class InsertDebtComponentImpl(
    private val repo: DebtRepository,
    componentContext: ComponentContext,
    private val navBack: () -> Unit
) : UpsertDebtComponentAbstract(componentContext, navBack) {
    override fun upsert() {
        scope.launch {
            repo.insert(
                with(state.value) {
                    DebtInsertModel(
                        name = name,
                        balance = moneyStringToLong(balance),
                    )
                }
            ).onSuccess {
                navBack()
            }
        }
    }

    override fun delete() {}
}

class UpdateDebtComponentImpl(
    private val repo: DebtRepository,
    componentContext: ComponentContext,
    private val navBack: () -> Unit,
    private val model: DebtModel
) : UpsertDebtComponentAbstract(componentContext, navBack) {
    override val state = MutableStateFlow(
        UpsertDebtComponent.State(
            name = model.name,
            balance = moneyLongToString(model.balance),
            isInsert = false
        )
    )

    override fun upsert() {
        scope.launch {
            repo.update(
                with(state.value) {
                    DebtModel(
                        id = model.id,
                        name = name,
                        balance = moneyStringToLong(balance),
                        updatedAt = model.updatedAt,
                        createdAt = model.createdAt,
                    )
                }
            ).onSuccess {
                navBack()
            }
        }
    }

    override fun delete() {
        if (state.value.confirmDelete) {
            scope.launch {
                repo.delete(model.id)
                    .onSuccess {
                        navBack()
                    }
            }
        } else {
            scope.launch {
                state.update { current -> current.copy(confirmDelete = true) }
                delay(1500)
                state.update { current -> current.copy(confirmDelete = false) }
            }
        }
    }
}
