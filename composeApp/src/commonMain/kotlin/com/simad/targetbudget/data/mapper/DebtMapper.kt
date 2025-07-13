package com.simad.targetbudget.data.mapper

import com.simad.targetbudget.data.database.entity.DebtEntity
import com.simad.targetbudget.data.helpers.generateUuid
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.DebtInsertModel
import com.simad.targetbudget.domain.model.DebtModel

class DebtMapper {
    fun toDomain(from: DebtEntity) = with(from) {
        DebtModel(
            id = id,
            name = name,
            balance = balance,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: DebtModel) = with(from) {
        DebtEntity(
            id = id,
            name = name,
            balance = balance,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: DebtInsertModel) = with(from) {
        DebtEntity(
            id = generateUuid(),
            name = name,
            balance = balance,
            updatedAt = nowInIso(),
            createdAt = nowInIso(),
        )
    }
}
