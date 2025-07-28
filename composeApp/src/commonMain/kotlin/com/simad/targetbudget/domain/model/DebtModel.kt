package com.simad.targetbudget.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DebtModel(
    val id: String,
    val name: String,
    val balance: Long,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class DebtInsertModel(
    val name: String,
    val balance: Long,
)
