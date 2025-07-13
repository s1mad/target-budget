package com.simad.targetbudget.data.database.dao

import androidx.room.*
import com.simad.targetbudget.data.database.entity.VirtualStorageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VirtualStorageDao {

    @Query("SELECT * FROM VirtualStorageEntity ORDER BY name ASC")
    fun getAll(): Flow<List<VirtualStorageEntity>>

    @Insert
    suspend fun insert(item: VirtualStorageEntity)

    @Update
    suspend fun update(item: VirtualStorageEntity)

    @Query("DELETE FROM VirtualStorageEntity WHERE id = :id")
    suspend fun delete(id: String)
}
