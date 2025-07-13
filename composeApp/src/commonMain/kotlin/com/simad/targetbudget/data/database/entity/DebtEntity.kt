package com.simad.targetbudget.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class DebtEntity(
    @PrimaryKey val id: String,
    val name: String,
    val balance: Long,
    val updatedAt: String,
    val createdAt: String,
)
