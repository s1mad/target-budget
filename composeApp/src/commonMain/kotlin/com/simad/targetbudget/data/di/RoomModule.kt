package com.simad.targetbudget.data.di

import androidx.room.Room
import com.simad.targetbudget.data.database.AppDatabase
import com.simad.targetbudget.data.database.dao.DebtDao
import com.simad.targetbudget.data.database.dao.RealStorageDao
import com.simad.targetbudget.data.database.dao.VirtualStorageDao
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

internal val RoomModule = DI.Module("DataRoomModule") {
    bindSingleton<AppDatabase> {
        Room.databaseBuilder(
            context = instance(),
            klass = AppDatabase::class.java,
            name = "target_budget.db"
        )
            .build()
    }

    bindSingleton<RealStorageDao>{
        instance<AppDatabase>().realStorageDao()
    }

    bindSingleton<VirtualStorageDao>{
        instance<AppDatabase>().virtualStorageDao()
    }

    bindSingleton<DebtDao>{
        instance<AppDatabase>().debtDao()
    }
}
