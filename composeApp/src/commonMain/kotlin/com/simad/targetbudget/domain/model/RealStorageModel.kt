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
    FULLY_ACCESSIBLE,
    ACCESSIBLE,
    HARDLY_ACCESSIBLE,
    INACCESSIBLE
}
