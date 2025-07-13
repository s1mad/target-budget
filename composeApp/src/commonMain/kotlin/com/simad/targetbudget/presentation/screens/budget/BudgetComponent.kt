package com.simad.targetbudget.presentation.screens.budget

import com.arkivanov.decompose.ComponentContext
import com.simad.targetbudget.domain.model.AccessibilityLevel
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
        val realStorages: List<RealStorageModel> = emptyList(),
        val realBalancesSum: Long = 0,
        val realStoragesFullyAccessibleSum: Long = 0,
        val realStoragesAccessibleSum: Long = 0,
        val virtualStorages: List<VirtualStorageModel> = emptyList(),
        val virtualBalancesSum: Long = 0,
        val debts: List<DebtModel> = emptyList(),
        val debtsPositiveSum: Long = 0,
        val debtsNegativeSum: Long = 0,
        val accessibleBalance: Long = 0,
        val buffer: Long = 0,
    )

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

    private fun updateAccessibleBalance() {
        state.update { current ->
            val accessibleBalance =
                current.realStoragesFullyAccessibleSum + current.realStoragesFullyAccessibleSum - (current.debtsPositiveSum + current.debtsNegativeSum)
            current.copy(
                accessibleBalance = accessibleBalance
            )
        }
    }

    private fun updateBuffer() {
        state.update { current ->
            val buffer =
                current.realBalancesSum + current.debtsPositiveSum + current.debtsNegativeSum - current.virtualBalancesSum
            current.copy(
                buffer = buffer
            )
        }
    }


    init {
        realStorageRepository.getAll()
            .distinctUntilChanged()
            .onEach { items ->
                val realBalancesSum = items.sumOf { it.balance }
                val realStoragesFullyAccessibleSum = items.sumOf {
                    if (it.accessibilityLevel == AccessibilityLevel.FULLY_ACCESSIBLE) it.balance else 0L
                }
                val realStoragesAccessibleSum = items.sumOf {
                    if (it.accessibilityLevel == AccessibilityLevel.ACCESSIBLE) it.balance else 0L
                }
                var bufferIsNeedUpdate = false
                var accessibleBalanceIsNeedUpdate = false
                state.update { current ->
                    bufferIsNeedUpdate = realBalancesSum != current.realBalancesSum
                    accessibleBalanceIsNeedUpdate =
                        realStoragesFullyAccessibleSum != current.realStoragesFullyAccessibleSum || realStoragesAccessibleSum != current.realStoragesAccessibleSum
                    current.copy(
                        realStorages = items,
                        realBalancesSum = realBalancesSum,
                        realStoragesFullyAccessibleSum = realStoragesFullyAccessibleSum,
                        realStoragesAccessibleSum = realStoragesAccessibleSum
                    )
                }
                if (bufferIsNeedUpdate) updateBuffer()
                if (accessibleBalanceIsNeedUpdate) updateAccessibleBalance()
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
                var bufferAndAccessibleBalanceIsNeedUpdate = false
                state.update { current ->
                    bufferAndAccessibleBalanceIsNeedUpdate =
                        debtsPositiveSum != current.debtsPositiveSum || debtsNegativeSum != current.debtsNegativeSum
                    current.copy(
                        debts = items,
                        debtsPositiveSum = debtsPositiveSum,
                        debtsNegativeSum = debtsNegativeSum
                    )
                }
                if (bufferAndAccessibleBalanceIsNeedUpdate) {
                    updateBuffer()
                    updateAccessibleBalance()
                }
            }
            .launchIn(scope)
    }

}