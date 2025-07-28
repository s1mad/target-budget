package com.simad.targetbudget.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RealStorageModel(
    val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val accessibility: Accessibility,
    val isArchived: Boolean,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class RealStorageInsertModel(
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val accessibility: Accessibility,
    val isArchived: Boolean,
)

@Serializable
enum class Accessibility {
    IMMEDIATE,          // можно получить в любой момент (карта, наличка)
    AVAILABLE,          // можно снять без потерь, но есть ограничения (накопительные, на ежедневный остаток)
    RESTRICTED,         // можно снять, но с потерями (вклады/накопительные, на мин. остаток)
    LOCKED              // нельзя снять (вклад без досрочного)
}
