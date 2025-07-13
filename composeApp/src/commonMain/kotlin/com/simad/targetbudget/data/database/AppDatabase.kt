package com.simad.targetbudget.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simad.targetbudget.data.database.dao.DebtDao
import com.simad.targetbudget.data.database.dao.RealStorageDao
import com.simad.targetbudget.data.database.dao.VirtualStorageDao
import com.simad.targetbudget.data.database.entity.DebtEntity
import com.simad.targetbudget.data.database.entity.RealStorageEntity
import com.simad.targetbudget.data.database.entity.VirtualStorageEntity
import com.simad.targetbudget.data.sources.db.RoomConverters

@Database(
    entities = [RealStorageEntity::class, VirtualStorageEntity::class, DebtEntity::class],
    version = 1
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun realStorageDao(): RealStorageDao
    abstract fun virtualStorageDao(): VirtualStorageDao
    abstract fun debtDao(): DebtDao
}
