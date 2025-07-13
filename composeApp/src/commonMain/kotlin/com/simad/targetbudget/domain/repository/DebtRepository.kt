package com.simad.targetbudget.domain.repository

import com.simad.targetbudget.domain.model.DebtInsertModel
import com.simad.targetbudget.domain.model.DebtModel
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAll(): Flow<List<DebtModel>>
    suspend fun insert(item: DebtInsertModel): Result<Unit>
    suspend fun update(item: DebtModel): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
}
