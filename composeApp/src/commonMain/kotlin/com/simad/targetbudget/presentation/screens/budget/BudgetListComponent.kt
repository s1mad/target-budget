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
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface BudgetListComponent {

    val state: StateFlow<State>
    fun onEvent(event: Event)

    data class State(
        val realStorageSection: RealStorageSection = RealStorageSection(),
        val virtualStorageSection: VirtualStorageSection = VirtualStorageSection(),
        val debtSection: DebtSection = DebtSection(),
    ) {
        data class RealStorageSection(
            val groups: List<RealStorageGroup> = emptyList(),
            val balances: Long = 0,
            val creditLimits: Long = 0,
        )

        data class RealStorageGroup(
            val accessibility: Accessibility,
            val balances: Long,
            val creditLimits: Long,
            val storages: List<RealStorageModel>
        )

        data class VirtualStorageSection(
            val storages: List<VirtualStorageModel> = emptyList(),
            val balances: Long = 0,
            val buffer: Long = 0,
        )

        data class DebtSection(
            val oweMe: DebtGroup = DebtGroup(),
            val oweI: DebtGroup = DebtGroup()
        )

        data class DebtGroup(
            val debts: List<DebtModel> = emptyList(),
            val balances: Long = 0,
        )
    }

    sealed interface Event {
        data object InsertRealStorage : Event
        data class UpdateRealStorage(val model: RealStorageModel) : Event
        data object InsertVirtualStorage : Event
        data class UpdateVirtualStorage(val model: VirtualStorageModel) : Event
        data object InsertDebt : Event
        data class UpdateDebt(val model: DebtModel) : Event
    }

    data class FactoryParams(
        val componentContext: ComponentContext,
        val navToUpsertRealStorage: (RealStorageModel?) -> Unit,
        val navToUpsertVirtualStorage: (VirtualStorageModel?) -> Unit,
        val navToUpsertDebt: (DebtModel?) -> Unit,
    )
}

class BudgetListComponentImpl(
    private val realStorageRepository: RealStorageRepository,
    private val virtualStorageRepository: VirtualStorageRepository,
    private val debtRepository: DebtRepository,
    componentContext: ComponentContext,
    private val navToUpsertRealStorage: (RealStorageModel?) -> Unit,
    private val navToUpsertVirtualStorage: (VirtualStorageModel?) -> Unit,
    private val navToUpsertDebt: (DebtModel?) -> Unit,
) : BudgetListComponent, ComponentContext by componentContext {

    override val state = MutableStateFlow(State())

    private val scope = componentCoroutineScope()

    override fun onEvent(event: BudgetListComponent.Event) {
        when (event) {
            BudgetListComponent.Event.InsertRealStorage -> navToUpsertRealStorage(null)
            is BudgetListComponent.Event.UpdateRealStorage -> navToUpsertRealStorage(event.model)
            BudgetListComponent.Event.InsertVirtualStorage -> navToUpsertVirtualStorage(null)
            is BudgetListComponent.Event.UpdateVirtualStorage -> navToUpsertVirtualStorage(event.model)
            BudgetListComponent.Event.InsertDebt -> navToUpsertDebt(null)
            is BudgetListComponent.Event.UpdateDebt -> navToUpsertDebt(event.model)
        }
    }

    private fun updateBuffer() {
        state.update { current ->
            val buffer = current.realStorageSection.balances + current.debtSection.oweMe.balances +
                    current.debtSection.oweI.balances - current.virtualStorageSection.balances
            current.copy(
                virtualStorageSection = current.virtualStorageSection.copy(buffer = buffer)
            )
        }
    }


    init {
        realStorageRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val groups = items
                    .groupBy { item -> item.accessibility }
                    .entries.map { entry ->
                        State.RealStorageGroup(
                            accessibility = entry.key,
                            balances = entry.value.sumOf { it.balance },
                            creditLimits = entry.value.sumOf { it.creditLimit },
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
                val balances = groups.sumOf { it.balances }
                val creditLimits = groups.sumOf { it.creditLimits }

                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate = balances != current.realStorageSection.balances
                    current.copy(
                        realStorageSection = current.realStorageSection.copy(
                            groups = groups,
                            balances = balances,
                            creditLimits = creditLimits
                        )
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)

        virtualStorageRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val balances = items.sumOf { it.balance }
                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate = balances != current.virtualStorageSection.balances
                    current.copy(
                        virtualStorageSection = current.virtualStorageSection.copy(
                            storages = items,
                            balances = balances
                        ),
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)

        debtRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val (negative, positive) = items.partition { it.balance < 0L }
                val positiveBalances = items.sumOf { if (it.balance > 0) it.balance else 0L }
                val negativeBalances = items.sumOf { if (it.balance < 0) it.balance else 0L }
                var bufferIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate =
                        positiveBalances != current.debtSection.oweMe.balances || negativeBalances != current.debtSection.oweI.balances
                    current.copy(
                        debtSection = current.debtSection.copy(
                            oweMe = current.debtSection.oweMe.copy(
                                debts = positive,
                                balances = positive.sumOf { it.balance }
                            ),
                            oweI = current.debtSection.oweI.copy(
                                debts = negative,
                                balances = negative.sumOf { it.balance }
                            )
                        ),
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
            }
            .launchIn(scope)
    }
}