package com.simad.targetbudget.data.mapper

import com.simad.targetbudget.data.database.entity.RealStorageEntity
import com.simad.targetbudget.data.helpers.generateUuid
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.RealStorageInsertModel
import com.simad.targetbudget.domain.model.RealStorageModel

class RealStorageMapper {
    fun toDomain(from: RealStorageEntity) = with(from) {
        RealStorageModel(
            id = id,
            name = name,
            balance = balance,
            creditLimit = creditLimit,
            accessibility = accessibility,
            isArchived = isArchived,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: RealStorageModel) = with(from) {
        RealStorageEntity(
            id = id,
            name = name,
            balance = balance,
            creditLimit = creditLimit,
            accessibility = accessibility,
            isArchived = isArchived,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: RealStorageInsertModel) = with(from) {
        RealStorageEntity(
            id = generateUuid(),
            name = name,
            balance = balance,
            creditLimit = creditLimit,
            accessibility = accessibility,
            isArchived = isArchived,
            updatedAt = nowInIso(),
            createdAt = nowInIso(),
        )
    }
}