package com.simad.targetbudget.presentation.screens.upsert_real_storage

import com.arkivanov.decompose.ComponentContext
import com.simad.targetbudget.domain.model.Accessibility
import com.simad.targetbudget.domain.model.RealStorageInsertModel
import com.simad.targetbudget.domain.model.RealStorageModel
import com.simad.targetbudget.domain.repository.RealStorageRepository
import com.simad.targetbudget.presentation.helpers.componentCoroutineScope
import com.simad.targetbudget.presentation.helpers.isValidMoneyStringInput
import com.simad.targetbudget.presentation.helpers.moneyLongToString
import com.simad.targetbudget.presentation.helpers.moneyStringToLong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UpsertRealStorageComponent {
    val state: StateFlow<State>
    fun onEvent(event: Event)

    data class State(
        val name: String = "",
        val balance: String = "",
        val creditLimit: String = "",
        val accessibility: Accessibility = Accessibility.IMMEDIATE,
        val isArchived: Boolean = false,
        val isInsert: Boolean = true,
        val confirmDelete: Boolean = false
    )

    sealed interface Event {
        data class ChangeName(val new: String) : Event
        data class ChangeBalance(val new: String) : Event
        data class ChangeCreditLimit(val new: String) : Event
        data class ChangeAccessibility(val new: Accessibility) : Event
        data class ChangeIsArchived(val new: Boolean) : Event
        data object Upsert : Event
        data object Delete : Event
        data object NavBack : Event
    }

    data class FactoryParams(
        val componentContext: ComponentContext,
        val navBack: () -> Unit,
        val model: RealStorageModel? = null
    )
}

abstract class UpsertRealStorageComponentAbstract(
    componentContext: ComponentContext,
    private val navBack: () -> Unit
) : UpsertRealStorageComponent, ComponentContext by componentContext {
    override val state = MutableStateFlow(UpsertRealStorageComponent.State())
    protected val scope = componentCoroutineScope()

    override fun onEvent(event: UpsertRealStorageComponent.Event) {
        when (event) {
            is UpsertRealStorageComponent.Event.ChangeName -> state.update { current ->
                current.copy(name = event.new)
            }

            is UpsertRealStorageComponent.Event.ChangeBalance -> if (isValidMoneyStringInput(event.new) || event.new.isEmpty()) {
                state.update { current -> current.copy(balance = event.new) }
            }

            is UpsertRealStorageComponent.Event.ChangeCreditLimit -> state.update { current ->
                current.copy(creditLimit = event.new)
            }

            is UpsertRealStorageComponent.Event.ChangeAccessibility -> state.update { current ->
                current.copy(accessibility = event.new)
            }

            is UpsertRealStorageComponent.Event.ChangeIsArchived -> state.update { current ->
                current.copy(isArchived = event.new)
            }

            UpsertRealStorageComponent.Event.Upsert -> if (state.value.name.isNotBlank()) upsert()
            UpsertRealStorageComponent.Event.Delete -> delete()
            UpsertRealStorageComponent.Event.NavBack -> navBack()
        }
    }

    abstract fun upsert()
    abstract fun delete()
}

class InsertRealStorageComponentImpl(
    private val repo: RealStorageRepository,
    componentContext: ComponentContext,
    private val navBack: () -> Unit
) : UpsertRealStorageComponentAbstract(componentContext, navBack) {
    override fun upsert() {
        scope.launch {
            repo.insert(
                with(state.value) {
                    RealStorageInsertModel(
                        name = name,
                        balance = moneyStringToLong(balance),
                        creditLimit = moneyStringToLong(creditLimit),
                        accessibility = accessibility,
                        isArchived = isArchived
                    )
                }
            ).onSuccess {
                navBack()
            }
        }
    }

    override fun delete() {}
}

class UpdateRealStorageComponentImpl(
    private val repo: RealStorageRepository,
    componentContext: ComponentContext,
    private val navBack: () -> Unit,
    private val model: RealStorageModel
) : UpsertRealStorageComponentAbstract(componentContext, navBack) {
    override val state = MutableStateFlow(
        UpsertRealStorageComponent.State(
            name = model.name,
            balance = moneyLongToString(model.balance),
            creditLimit = moneyLongToString(model.creditLimit),
            accessibility = model.accessibility,
            isArchived = model.isArchived,
            isInsert = false
        )
    )

    override fun upsert() {
        scope.launch {
            repo.update(
                with(state.value) {
                    RealStorageModel(
                        id = model.id,
                        name = name,
                        balance = moneyStringToLong(balance),
                        creditLimit = moneyStringToLong(creditLimit),
                        accessibility = accessibility,
                        isArchived = isArchived,
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