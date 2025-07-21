package com.simad.targetbudget.presentation.helpers

import com.simad.targetbudget.domain.model.RealStorageModel

fun Long.balanceToString(): String {
    val str = this.toString()
    return when {
        str.length > 2 -> {
            val rubles = str.dropLast(2)
            val pennies = str.takeLast(2).let {
                if (it == "00") ""
                else if (it.endsWith("0")) it.dropLast(1)
                else it
            }
            if (pennies.isEmpty()) "$rubles ₽"
            else "$rubles,$pennies ₽"
        }
        str.length == 2 -> {
            val pennies = str.let {
                if (it == "00") ""
                else if (it.endsWith("0")) it.dropLast(1)
                else it
            }
            if (pennies.isEmpty()) "0 ₽"
            else "0,$pennies ₽"
        }
        else -> "0,0$str ₽"
    }
}

fun RealStorageModel.balanceToString(): String = balance.balanceToString()
