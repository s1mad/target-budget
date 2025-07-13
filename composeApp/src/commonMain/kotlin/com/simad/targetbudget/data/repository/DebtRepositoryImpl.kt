package com.simad.targetbudget.data.repository

import com.simad.targetbudget.data.database.dao.DebtDao
import com.simad.targetbudget.data.mapper.DebtMapper
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.DebtInsertModel
import com.simad.targetbudget.domain.model.DebtModel
import com.simad.targetbudget.domain.repository.DebtRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class DebtRepositoryImpl(
    private val dao: DebtDao,
    private val mapper: DebtMapper,
) : DebtRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<DebtModel>> {
        return dao.getAll().mapLatest { list ->
            list.map { entity ->
                mapper.toDomain(entity)
            }
        }
    }

    override suspend fun insert(item: DebtInsertModel): Result<Unit> = runCatching {
        dao.insert(item = mapper.toData(item))
    }


    override suspend fun update(item: DebtModel): Result<Unit> = runCatching {
        dao.update(item = mapper.toData(item).copy(updatedAt = nowInIso()))
    }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        dao.delete(id = id)
    }
}
