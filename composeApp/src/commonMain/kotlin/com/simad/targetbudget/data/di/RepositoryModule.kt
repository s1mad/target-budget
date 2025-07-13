package com.simad.targetbudget.data.di

import com.simad.targetbudget.data.repository.DebtRepositoryImpl
import com.simad.targetbudget.data.repository.RealStorageRepositoryImpl
import com.simad.targetbudget.data.repository.VirtualStorageRepositoryImpl
import com.simad.targetbudget.domain.repository.DebtRepository
import com.simad.targetbudget.domain.repository.RealStorageRepository
import com.simad.targetbudget.domain.repository.VirtualStorageRepository
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

internal val RepositoryModule = DI.Module("DataRepositoryModule") {
    bindSingleton<RealStorageRepository> {
        RealStorageRepositoryImpl(
            dao = instance(),
            mapper = instance()
        )
    }

    bindSingleton<VirtualStorageRepository> {
        VirtualStorageRepositoryImpl(
            dao = instance(),
            mapper = instance()
        )
    }

    bindSingleton<DebtRepository> {
        DebtRepositoryImpl(
            dao = instance(),
            mapper = instance()
        )
    }
}
