package com.simad.targetbudget.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.simad.targetbudget.data.database.entity.RealStorageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RealStorageDao {

    @Query("SELECT * FROM RealStorageEntity ORDER BY name ASC")
    fun getAll(): Flow<List<RealStorageEntity>>

    @Insert
    suspend fun insert(item: RealStorageEntity)

    @Update
    suspend fun update(item: RealStorageEntity)

    @Query("DELETE FROM RealStorageEntity WHERE id = :id")
    suspend fun delete(id: String)
}
