package com.simad.targetbudget.domain.model

data class DebtModel(
    val id: String,
    val name: String,
    val balance: Long,
    val updatedAt: String,
    val createdAt: String
)

data class DebtInsertModel(
    val name: String,
    val balance: Long,
)

