package com.simad.targetbudget.data.repository

import com.simad.targetbudget.data.database.dao.RealStorageDao
import com.simad.targetbudget.data.mapper.RealStorageMapper
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.RealStorageInsertModel
import com.simad.targetbudget.domain.model.RealStorageModel
import com.simad.targetbudget.domain.repository.RealStorageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class RealStorageRepositoryImpl(
    private val dao: RealStorageDao,
    private val mapper: RealStorageMapper,
) : RealStorageRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<RealStorageModel>> {
        return dao.getAll().mapLatest { list ->
            list.map { entity ->
                mapper.toDomain(entity)
            }
        }
    }

    override suspend fun insert(item: RealStorageInsertModel): Result<Unit> = runCatching {
        dao.insert(item = mapper.toData(item))
    }


    override suspend fun update(item: RealStorageModel): Result<Unit> = runCatching {
        dao.update(item = mapper.toData(item).copy(updatedAt = nowInIso()))
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        dao.delete(id = id)
    }
}
