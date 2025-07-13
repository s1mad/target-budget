package com.simad.targetbudget.data.database.dao

import androidx.room.*
import com.simad.targetbudget.data.database.entity.DebtEntity
import com.simad.targetbudget.data.database.entity.VirtualStorageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {

    @Query("SELECT * FROM DebtEntity ORDER BY name ASC")
    fun getAll(): Flow<List<DebtEntity>>

    @Insert
    suspend fun insert(item: DebtEntity)

    @Update
    suspend fun update(item: DebtEntity)

    @Query("DELETE FROM DebtEntity WHERE id = :id")
    suspend fun delete(id: String)
}
