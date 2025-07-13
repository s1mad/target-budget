package com.simad.targetbudget.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class VirtualStorageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val balance: Long,
    val description: String?,
    val updatedAt: String,
    val createdAt: String,
)
