package com.simad.targetbudget.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VirtualStorageModel(
    val id: String,
    val name: String,
    val balance: Long,
    val description: String?,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class VirtualStorageInsertModel(
    val name: String,
    val balance: Long,
    val description: String?,
)
