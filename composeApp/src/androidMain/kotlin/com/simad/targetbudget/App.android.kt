package com.simad.targetbudget

import android.R.attr.x
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.simad.targetbudget.di.DiProvider
import org.kodein.di.bindSingleton

class TargetBudgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }
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