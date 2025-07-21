package com.simad.targetbudget.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.simad.targetbudget.domain.model.Accessibility

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class RealStorageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val accessibility: Accessibility,
    val isArchived: Boolean,
    val updatedAt: String,
    val createdAt: String,
)
