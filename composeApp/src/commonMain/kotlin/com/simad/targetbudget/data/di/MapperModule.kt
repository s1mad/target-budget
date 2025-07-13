package com.simad.targetbudget.data.di

import com.simad.targetbudget.data.mapper.DebtMapper
import com.simad.targetbudget.data.mapper.RealStorageMapper
import com.simad.targetbudget.data.mapper.VirtualStorageMapper
import org.kodein.di.DI
import org.kodein.di.bindSingleton

internal val MapperModule = DI.Module("DataMapperModule") {
    bindSingleton<RealStorageMapper> {
        RealStorageMapper()
    }

    bindSingleton<VirtualStorageMapper> {
        VirtualStorageMapper()
    }

    bindSingleton<DebtMapper> {
        DebtMapper()
    }
}
