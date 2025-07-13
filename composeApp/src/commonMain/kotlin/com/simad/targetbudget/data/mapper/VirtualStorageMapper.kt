package com.simad.targetbudget.data.mapper

import com.simad.targetbudget.data.database.entity.VirtualStorageEntity
import com.simad.targetbudget.data.helpers.generateUuid
import com.simad.targetbudget.data.helpers.nowInIso
import com.simad.targetbudget.domain.model.VirtualStorageInsertModel
import com.simad.targetbudget.domain.model.VirtualStorageModel

class VirtualStorageMapper {
    fun toDomain(from: VirtualStorageEntity) = with(from) {
        VirtualStorageModel(
            id = id,
            name = name,
            balance = balance,
            description = description,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: VirtualStorageModel) = with(from) {
        VirtualStorageEntity(
            id = id,
            name = name,
            balance = balance,
            description = description,
            updatedAt = updatedAt,
            createdAt = createdAt,
        )
    }

    fun toData(from: VirtualStorageInsertModel) = with(from) {
        VirtualStorageEntity(
            id = generateUuid(),
            name = name,
            balance = balance,
            description = description,
            updatedAt = nowInIso(),
            createdAt = nowInIso(),
        )
    }
}