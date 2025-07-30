package com.simad.targetbudget.presentation.helpers

import com.simad.targetbudget.domain.model.DebtModel
import com.simad.targetbudget.domain.model.RealStorageModel
import com.simad.targetbudget.domain.model.VirtualStorageModel
import com.simad.targetbudget.presentation.screens.budget.BudgetListComponent

fun isValidMoneyStringInput(value: String): Boolean = value.matches(Regex("""^-?\d{0,15}([.,]\d{0,2})?$"""))

fun moneyLongToString(value: Long): String {
    val absValue = kotlin.math.abs(value)
    val majorUnits = absValue / 100
    val minorUnits = absValue % 100
    val sign = if (value < 0) "-" else ""
    return when {
        minorUnits == 0L -> "$sign$majorUnits"
        minorUnits / 10 == 0L -> "$sign$majorUnits.0${minorUnits % 10}"
        minorUnits % 10 == 0L -> "$sign$majorUnits.${minorUnits / 10}"
        else -> "$sign$majorUnits.$minorUnits"
    }
}

fun moneyStringToLong(input: String): Long {
    val normalized = input.replace(",", ".").trim()
    return when {
        normalized.contains(".") -> {
            val parts = normalized.split(".")
            val majorUnits = parts.getOrNull(0)?.toLongOrNull() ?: 0L
            val minorUnits = parts.getOrNull(1)?.padEnd(2, '0')?.take(2)?.toLongOrNull() ?: 0L
            majorUnits * 100L + minorUnits
        }

        else -> (normalized.toLongOrNull() ?: 0L) * 100
    }
}

fun moneyLongToEasyReadString(value: Long): String {
    val moneyString = moneyLongToString(value)
    val parts = moneyString.split(".")
    var integerPart = parts[0]

    val isNegative = integerPart.startsWith("-")
    if (isNegative) {
        integerPart = integerPart.substring(1)
    }

    val formattedInteger = integerPart.reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    val resultInteger = if (isNegative) "-$formattedInteger" else formattedInteger

    return if (parts.size > 1) {
        "$resultInteger.${parts[1]}"
    } else {
        resultInteger
    }
}


fun RealStorageModel.balanceEasyRead(): String = moneyLongToEasyReadString(balance) + " ₽"
fun BudgetListComponent.State.RealStorageGroup.balancesEasyRead(): String = moneyLongToEasyReadString(balances) + " ₽"
fun BudgetListComponent.State.RealStorageSection.balancesEasyRead(): String = moneyLongToEasyReadString(balances) + " ₽"

fun VirtualStorageModel.balanceEasyRead(): String = moneyLongToEasyReadString(balance) + " ₽"
fun BudgetListComponent.State.VirtualStorageSection.balancesEasyRead(): String = moneyLongToEasyReadString(balances) + " ₽"
fun BudgetListComponent.State.VirtualStorageSection.bufferEasyRead(): String = moneyLongToEasyReadString(buffer) + " ₽"

fun DebtModel.balanceEasyRead(): String = moneyLongToEasyReadString(balance) + " ₽"
fun BudgetListComponent.State.DebtSection.oweMeBalancesEasyRead(): String = moneyLongToEasyReadString(oweMe.balances) + " ₽"
fun BudgetListComponent.State.DebtSection.oweIBalancesEasyRead(): String = moneyLongToEasyReadString(oweI.balances) + " ₽"