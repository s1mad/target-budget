package com.simad.targetbudget.domain.repository

import com.simad.targetbudget.domain.model.VirtualStorageInsertModel
import com.simad.targetbudget.domain.model.VirtualStorageModel
import kotlinx.coroutines.flow.Flow

interface VirtualStorageRepository {
    fun getAll(): Flow<List<VirtualStorageModel>>
    suspend fun insert(item: VirtualStorageInsertModel): Result<Unit>
    suspend fun update(item: VirtualStorageModel): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
}
