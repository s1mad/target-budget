package com.simad.targetbudget.domain.model

data class RealStorageModel(
    val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val accessibilityLevel: AccessibilityLevel,
    val isArchived: Boolean,
    val updatedAt: String,
    val createdAt: String
)

data class RealStorageInsertModel(
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val accessibilityLevel: AccessibilityLevel,
    val isArchived: Boolean,
)

enum class AccessibilityLevel {
    FULLY_ACCESSIBLE,          // доступны в любой момент (карта, наличка)
    ACCESSIBLE,                // можно снять без потерь, но с ограничениями (накопительные, на ежедневный остаток)
    HARDLY_ACCESSIBLE,         // есть штрафы или лимиты (вклады/накопительные, на мин. остаток)
    INACCESSIBLE               // нельзя снять (вклад без досрочного)
}
