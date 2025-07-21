package com.simad.targetbudget

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.simad.targetbudget.di.DiProvider
import com.simad.targetbudget.presentation.screens.budget.BudgetComponent
import org.kodein.di.bindSingleton

class TargetBudgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val rootContext = DefaultComponentContext(lifecycle = lifecycle)
        val budgetComponentFactory: (ComponentContext) -> BudgetComponent = DiProvider.factory()
        val budgetComponent = budgetComponentFactory(rootContext)
        setContent { App(budgetComponent) }
    }
}

class TargetBudgetApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DiProvider.init {
            bindSingleton<Context> { this@TargetBudgetApplication }
        }
    }
}