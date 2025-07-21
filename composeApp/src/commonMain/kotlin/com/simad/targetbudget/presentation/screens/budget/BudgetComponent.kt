package com.simad.targetbudget.presentation.screens.budget

import com.arkivanov.decompose.ComponentContext
import com.simad.targetbudget.domain.model.Accessibility
import com.simad.targetbudget.domain.model.DebtModel
import com.simad.targetbudget.domain.model.RealStorageModel
import com.simad.targetbudget.domain.model.VirtualStorageModel
import com.simad.targetbudget.domain.repository.DebtRepository
import com.simad.targetbudget.domain.repository.RealStorageRepository
import com.simad.targetbudget.domain.repository.VirtualStorageRepository
import com.simad.targetbudget.presentation.helpers.componentCoroutineScope
import com.simad.targetbudget.presentation.screens.budget.BudgetComponent.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface BudgetComponent {

    val state: StateFlow<State>
    fun onEvent(event: Event)

    data class State(
        val realStorageSection: RealStorageSection = RealStorageSection(),

        val virtualStorages: List<VirtualStorageModel> = emptyList(),
        val virtualBalancesSum: Long = 0,

        val debts: List<DebtModel> = emptyList(),
        val debtsPositiveSum: Long = 0,
        val debtsNegativeSum: Long = 0,

        val buffer: Long = 0,
    ) {
        data class RealStorageSection(
            val groups: List<RealStorageGroup> = emptyList(),
            val balances: Long = 0,
        )

        data class RealStorageGroup(
            val accessibility: Accessibility,
            val balances: Long,
            val storages: List<RealStorageModel>
        )
    }

    sealed class Event {

    }
}

class BudgetComponentImpl(
    componentContext: ComponentContext,
    private val realStorageRepository: RealStorageRepository,
    private val virtualStorageRepository: VirtualStorageRepository,
    private val debtRepository: DebtRepository,
) : BudgetComponent, ComponentContext by componentContext {

    override val state = MutableStateFlow(State())

    private val scope = componentCoroutineScope()

    override fun onEvent(event: BudgetComponent.Event) {}

    private fun updateBuffer() {
        state.update { current ->
            val buffer = current.realStorageSection.balances + current.debtsPositiveSum +
                    current.debtsNegativeSum - current.virtualBalancesSum
            current.copy(
                buffer = buffer
            )
        }
    }


    init {
        realStorageRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->

                val realStorages = items
                    .groupBy { item -> item.accessibility }
                    .entries.map { entry ->
                        State.RealStorageGroup(
                            accessibility = entry.key,
                            balances = entry.value.sumOf { it.balance },
                            storages = entry.value
                        )
                    }
                    .sortedBy {
                        when (it.accessibility) {
                            Accessibility.IMMEDIATE -> 0
                            Accessibility.AVAILABLE -> 1
                            Accessibility.RESTRICTED -> 2
                            Accessibility.LOCKED -> 3
                        }
                    }
                val balances = realStorages.sumOf { it.balances }

                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate = balances != current.realStorageSection.balances
                    current.copy(
                        realStorageSection = State.RealStorageSection(
                            groups = realStorages,
                            balances = balances,
                        )

                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)

        virtualStorageRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val virtualBalancesSum = items.sumOf { it.balance }
                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate = virtualBalancesSum != current.virtualBalancesSum
                    current.copy(
                        virtualStorages = items,
                        virtualBalancesSum = virtualBalancesSum
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)

        debtRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val debtsPositiveSum = items.sumOf { if (it.balance > 0) it.balance else 0L }
                val debtsNegativeSum = items.sumOf { if (it.balance < 0) it.balance else 0L }
                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate =
                        debtsPositiveSum != current.debtsPositiveSum || debtsNegativeSum != current.debtsNegativeSum
                    current.copy(
                        debts = items,
                        debtsPositiveSum = debtsPositiveSum,
                        debtsNegativeSum = debtsNegativeSum
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)
    }
}