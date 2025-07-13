package com.simad.targetbudget.domain.model

data class VirtualStorageModel(
    val id: String,
    val name: String,
    val balance: Long,
    val description: String?,
    val updatedAt: String,
    val createdAt: String
)

data class VirtualStorageInsertModel(
    val name: String,
    val balance: Long,
    val description: String?,
)
