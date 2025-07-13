package com.simad.targetbudget.data.repository

import com.simad.targetbudget.data.database.dao.VirtualStorageDao
import com.simad.targetbudget.data.mapper.VirtualStorageMapper
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.VirtualStorageInsertModel
import com.simad.targetbudget.domain.model.VirtualStorageModel
import com.simad.targetbudget.domain.repository.VirtualStorageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class VirtualStorageRepositoryImpl(
    private val dao: VirtualStorageDao,
    private val mapper: VirtualStorageMapper,
) : VirtualStorageRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<VirtualStorageModel>> {
        return dao.getAll().mapLatest { list ->
            list.map { entity ->
                mapper.toDomain(entity)
            }
        }
    }

    override suspend fun insert(item: VirtualStorageInsertModel): Result<Unit> = runCatching {
        dao.insert(mapper.toData(item))
    }

    override suspend fun update(item: VirtualStorageModel): Result<Unit> = runCatching {
        dao.update(mapper.toData(item).copy(updatedAt = nowInIso()))
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        dao.delete(id)
    }
}
