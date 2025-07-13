package com.simad.targetbudget.domain.repository

import com.simad.targetbudget.domain.model.RealStorageInsertModel
import com.simad.targetbudget.domain.model.RealStorageModel
import kotlinx.coroutines.flow.Flow

interface RealStorageRepository {
    fun getAll(): Flow<List<RealStorageModel>>
    suspend fun insert(item: RealStorageInsertModel): Result<Unit>
    suspend fun update(item: RealStorageModel): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
}
